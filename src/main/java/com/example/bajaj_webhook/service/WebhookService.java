package com.example.bajaj_webhook.service;

import com.example.bajaj_webhook.model.FinalSubmissionRequest;
import com.example.bajaj_webhook.model.GenerateWebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebhookService {

    private static final Logger log = LoggerFactory.getLogger(WebhookService.class);

    private final RestTemplate rest;

    @Value("${bfhl.generateWebhookUrl}")
    private String generateUrl;

    public WebhookService(RestTemplate rest) {
        this.rest = rest;
    }

    public GenerateWebhookResponse generateWebhook(String name, String regNo, String email) {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("regNo", regNo);
        body.put("email", email);

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<GenerateWebhookResponse> resp =
                    rest.postForEntity(generateUrl, new HttpEntity<>(body, h), GenerateWebhookResponse.class);

            if (resp.getStatusCode().is2xxSuccessful()) return resp.getBody();
        } catch (Exception e) {
            log.error("Error generating webhook: {}", e.getMessage());
        }
        return null;
    }

    public boolean submitFinalQuery(String webhook, String token, String sql) {
        FinalSubmissionRequest req = new FinalSubmissionRequest(sql);

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.set("Authorization", token);

        try {
            ResponseEntity<String> resp =
                    rest.postForEntity(webhook, new HttpEntity<>(req, h), String.class);

            return resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Submit error: {}", e.getMessage());
        }
        return false;
    }
}
