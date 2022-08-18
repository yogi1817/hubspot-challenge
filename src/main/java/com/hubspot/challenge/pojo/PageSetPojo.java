package com.hubspot.challenge.pojo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PageSetPojo {
    private long timestamp;
    private String event;
}
