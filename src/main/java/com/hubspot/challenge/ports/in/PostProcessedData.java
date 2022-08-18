package com.hubspot.challenge.ports.in;

import com.hubspot.challenge.dataset.dto.Events;

import java.util.List;

public interface PostProcessedData {
    List<Events> postData(String userKey);
}
