package com.example.fintech.webhook.dummy.stub;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FintechWebhookDummyStubApplication {

    @Value("${last.request.cache.size:10}")
    private int lastRequestsCacheSize;

    public static void main(String[] args) {
        SpringApplication.run(FintechWebhookDummyStubApplication.class, args);
    }

    @Bean("lastRequestsCache")
    public CircularFifoQueue<RequestInfo> lastRequestsCache() {
        return new CircularFifoQueue<>(lastRequestsCacheSize);
    }

}
