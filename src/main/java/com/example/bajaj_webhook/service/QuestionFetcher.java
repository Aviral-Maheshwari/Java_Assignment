package com.example.bajaj_webhook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class QuestionFetcher {

    private static final Logger log = LoggerFactory.getLogger(QuestionFetcher.class);

    @Value("${bfhl.solutionFile}")
    private String solutionFile;

    public String loadFinalSql() {
        try {
            Path p = Path.of(solutionFile);
            if (Files.exists(p)) {
                log.info("Loading final SQL from {}", solutionFile);
                return Files.readString(p);
            }
        } catch (Exception e) {
            log.error("Error reading SQL file: {}", e.getMessage());
        }
        return null;
    }
}
