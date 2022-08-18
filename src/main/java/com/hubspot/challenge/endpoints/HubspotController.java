package com.hubspot.challenge.endpoints;

import com.hubspot.challenge.api.HubspotApiDelegate;
import com.hubspot.challenge.ports.in.ProcessEventData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HubspotController implements HubspotApiDelegate {

    private final ProcessEventData processEventData;

    @Override
    public ResponseEntity<Void> getAndProcessData(String userKey) {
        processEventData.processEventData(userKey);
        return HubspotApiDelegate.super.getAndProcessData(userKey);
    }
}
