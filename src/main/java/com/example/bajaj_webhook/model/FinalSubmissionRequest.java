package com.example.bajaj_webhook.model;

public class FinalSubmissionRequest {

    private String finalQuery;

    public FinalSubmissionRequest() {}

    public FinalSubmissionRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    public String getFinalQuery() {
        return finalQuery;
    }

    public void setFinalQuery(String finalQuery) {
        this.finalQuery = finalQuery;
    }
}
