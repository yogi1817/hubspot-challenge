package com.hubspot.challenge.ports.in;

import com.hubspot.challenge.dataset.dto.HubspotClientApiResponse;

public interface ReadEventData {
    HubspotClientApiResponse readEventData(String userKey);
}
