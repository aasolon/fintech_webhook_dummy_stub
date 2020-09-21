package com.example.fintech.webhook.dummy.stub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
public class FintechWebhookDummyController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${success.response.percent:80}")
    private int successResponsePercent;

    @Value("${health.check.gif:}")
    private String healthCheckGif;

    private static final FastDateFormat FAST_DATE_FORMAT = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");

    private final CircularFifoQueue<RequestInfo> lastRequestsCache;

    private final ObjectMapper objectMapper;

    public FintechWebhookDummyController(@Qualifier("lastRequestsCache")
                                         CircularFifoQueue<RequestInfo> lastRequestsCache,
                                         ObjectMapper objectMapper) {
        this.lastRequestsCache = lastRequestsCache;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody String body) {
        Random random = new Random();
        int randomValue = random.nextInt(100);
        boolean isOkResponse = randomValue < successResponsePercent;
        String response = isOkResponse ? "<font color=\"green\">200 OK  &#128578;</font>" : "<font color=\"red\">500 INTERNAL SERVER ERROR</font>  &#128561;";
        RequestInfo requestInfo = new RequestInfo(System.currentTimeMillis(), body, response);
        lastRequestsCache.add(requestInfo);

        String bodyAsPrettyJsonString;
        try {
            Object bodyAsJsonObject = objectMapper.readValue(requestInfo.getBody(), Object.class);
            bodyAsPrettyJsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bodyAsJsonObject);
        } catch (JsonProcessingException e) {
            bodyAsPrettyJsonString = requestInfo.getBody();
        }
        String logString = "\n" +
                "Response: " + (isOkResponse ? "200 OK" : "500 INTERNAL SERVER ERROR") + "\n" +
                "Request body:\n" + bodyAsPrettyJsonString;
        log.info(logString);

        if (isOkResponse) {
            return ResponseEntity.ok().body("It's ok!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ooops, server error :(");
        }
    }

    @GetMapping("/last-requests")
    public String get() {
        if (lastRequestsCache.isEmpty())
            return "Last requests cache is empty :(";
        List<RequestInfo> lastRequestsCacheList = new ArrayList<>(lastRequestsCache);
        Collections.reverse(lastRequestsCacheList);
        StringBuilder builder = new StringBuilder();
        for (RequestInfo requestInfo : lastRequestsCacheList) {
            Date date = new Date(requestInfo.getTimestamp());
            String dateStr = FAST_DATE_FORMAT.format(date);

            builder.append("<b>Time:</b> <font color=\"blue\">").append(dateStr).append("</font>");
            builder.append("<br>");
            builder.append("<b>Request body:</b>").append(requestInfo.getBody());
            builder.append("<br>");
            builder.append("<b>Response:</b> ").append(requestInfo.getResponse());
            builder.append("<br>");
            builder.append("========================================");
            builder.append("<br>");
        }
        return builder.toString();
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://ya.ru").replaceQueryParam("requestId", "123321");
        builder.replaceQueryParam("requestId", "22");
        builder.replaceQueryParam("requestId", "33");
        if (StringUtils.isNotEmpty(healthCheckGif)) {
            return "Application is running" +
                    "<br><br>" +
                    "<img src='" + healthCheckGif + "'/>";
        } else {
            return "Application is running";
        }
    }
}
