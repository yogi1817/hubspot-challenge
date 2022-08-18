package com.hubspot.challenge.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class SessionData {
    private long duration;
    private List<String> pages;
    private long startTime;
    @JsonIgnore
    private int order;
}
