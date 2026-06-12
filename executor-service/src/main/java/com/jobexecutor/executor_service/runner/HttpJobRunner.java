package com.jobexecutor.executor_service.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class HttpJobRunner implements JobRunner {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String run(String payload) throws Exception {
        try {
            Map map = objectMapper.readValue(payload, Map.class);
            String url = (String) map.get("url");
            if (url != null) {
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                return response.getStatusCode().toString();
            }
        } catch (Exception e) {
            System.err.println("Failed to parse or execute HTTP call: " + e.getMessage());
        }
        return "SUCCESS";
    }
}