package com.hubspot.challenge.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SessionByUser {
    Map<String, List<SessionData>> sessionsByUser;
}
