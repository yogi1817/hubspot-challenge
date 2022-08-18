package com.hubspot.challenge.adapters;

import ch.qos.logback.core.net.ObjectWriter;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.challenge.config.ServiceConfig;
import com.hubspot.challenge.dataset.dto.Events;
import com.hubspot.challenge.dataset.dto.HubspotClientApiResponse;
import com.hubspot.challenge.pojo.PageSetPojo;
import com.hubspot.challenge.pojo.SessionByUser;
import com.hubspot.challenge.pojo.SessionData;
import com.hubspot.challenge.ports.in.ProcessEventData;
import com.hubspot.challenge.ports.in.ReadEventData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubspotAdapter implements ProcessEventData {

    private final ReadEventData readEventData;
    private final RestTemplate restTemplate;
    private final ServiceConfig serviceConfig;
    @Override
    public void processEventData(String userKey) {
        Map<String, List<Events>> eventMap = new HashMap<>();
        List<Events> eventListForUser = new ArrayList<>();
        HubspotClientApiResponse readData = readEventData.readEventData(userKey);
        int count = 0;
        for (Events eventData : readData.getEvents()) {
            eventData.setOrder(count++);
            if(eventMap.containsKey(eventData.getVisitorId())){
                eventListForUser = eventMap.get(eventData.getVisitorId());
            }else{
                eventListForUser = new ArrayList<>();
            }
            eventListForUser.add(eventData);
            eventMap.put(eventData.getVisitorId(), eventListForUser);
        }

        List<Events> sessionDataInOrder = new ArrayList<>();
        Long nextTimestamp = 0L;
        Map<String, List<SessionData>> sessionDataMap = new HashMap<>();
        for (Map.Entry<String, List<Events>> entry : eventMap.entrySet()) {
            entry.getValue().sort(Comparator.comparing(Events::getTimestamp));
            List<String> pageSet = new ArrayList<>();
            List<PageSetPojo> pageSetPojos = new ArrayList<>();
            Long startTime = 0L;
            Long endTime = 0L;
            Long lastRecordedTime = 0l;
            SessionData sessionData = null;
            List<SessionData> sessionDataList = new ArrayList<>();

            int order = 0;
            for(Events event: entry.getValue()) {
                order = event.getOrder();
                if (startTime == 0L){
                    startTime = event.getTimestamp();
                    endTime = event.getTimestamp() + 600000;
                }

                if (event.getTimestamp() > endTime) {
                    pageSetPojos.sort(Comparator.comparing(PageSetPojo::getTimestamp));
                    sessionData = SessionData.builder()
                            .pages(pageSetPojos.stream().map(PageSetPojo::getEvent).collect(Collectors.toList()))
                            .startTime(startTime)
                            .duration(lastRecordedTime - startTime)
                            .order(order)
                            .build();
                    sessionDataList.add(sessionData);

                    startTime = event.getTimestamp();
                    endTime = event.getTimestamp() + 600000;
                    pageSet = new ArrayList<>();
                    pageSetPojos = new ArrayList<>();
                }
                pageSet.add(event.getUrl());
                pageSetPojos.add(PageSetPojo.builder()
                                .timestamp(event.getTimestamp())
                                .event(event.getUrl())
                        .build());
                lastRecordedTime = event.getTimestamp();
            }
            sessionData = SessionData.builder()
                    .pages(new ArrayList<>(pageSet))
                    .startTime(startTime)
                    .duration(lastRecordedTime - startTime)
                    .order(order)
                    .build();

            sessionDataList.add(sessionData);
            sessionDataList.sort(Comparator.comparing(SessionData::getStartTime));
            sessionDataMap.put(entry.getValue().get(0).getVisitorId(), sessionDataList);
        }

        SessionByUser sessionByUser = SessionByUser.builder()
                .sessionsByUser(sessionDataMap)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        try {
            // convert user object to json string and return it
             log.error(mapper.writeValueAsString(sessionByUser));
        }
        catch (JsonGenerationException | JsonMappingException e) {
            // catch various errors
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        restTemplate.postForObject(serviceConfig.getPostHost(), sessionByUser, SessionByUser.class);
        log.debug(String.valueOf(eventMap));
    }
}
