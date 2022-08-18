package com.hubspot.challenge.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {
    private final LoggingInterceptor loggingInterceptor;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                //.interceptors(loggingInterceptor)
                .build();
    }
}
