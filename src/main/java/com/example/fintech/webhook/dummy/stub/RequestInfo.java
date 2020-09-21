package com.example.fintech.webhook.dummy.stub;

public class RequestInfo {

    private final long timestamp;

    private final String body;

    private final String response;

    public RequestInfo(long timestamp, String body, String response) {
        this.timestamp = timestamp;
        this.body = body;
        this.response = response;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getBody() {
        return body;
    }

    public String getResponse() {
        return response;
    }
}
