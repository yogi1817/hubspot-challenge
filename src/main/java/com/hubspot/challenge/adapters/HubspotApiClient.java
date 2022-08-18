package com.hubspot.challenge.adapters;

import com.hubspot.challenge.config.ServiceConfig;
import com.hubspot.challenge.dataset.api.HubspotApi;
import com.hubspot.challenge.dataset.dto.HubspotClientApiResponse;
import com.hubspot.challenge.ports.in.ReadEventData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@RequiredArgsConstructor
public class HubspotApiClient implements ReadEventData {

    private final HubspotApi hubspotApi;
    private final ServiceConfig serviceConfig;

    @PostConstruct
    public void postConstruct() {
        hubspotApi.getApiClient()
                .setBasePath(serviceConfig.getHost());
    }

    @Override
    public HubspotClientApiResponse readEventData(String userKey) {
        return hubspotApi.getHubspotEventData(userKey);
    }
}
