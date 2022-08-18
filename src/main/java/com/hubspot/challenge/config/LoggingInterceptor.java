package com.hubspot.challenge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Configuration
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest req, byte[] reqBody, ClientHttpRequestExecution ex) throws IOException {
        ClientHttpResponse response = ex.execute(req, reqBody);
        InputStreamReader isr = new InputStreamReader(
                response.getBody(), StandardCharsets.UTF_8);
        String body = new BufferedReader(isr).lines()
                .collect(Collectors.joining("\n"));
        return response;
    }
}
