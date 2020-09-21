package com.example.fintech.webhook.dummy.stub;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.File;
import java.util.Properties;

public class ServletInitializer extends SpringBootServletInitializer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        String configFilePath = System.getProperty("fintech.webhook.dummy.stub.config.file.path");
        if (StringUtils.isNotEmpty(configFilePath)) {
            log.info("Starting with config file: " + configFilePath);
            Properties props = new Properties();
            props.setProperty("spring.config.location", configFilePath); //set the config file to use
            application.application().setDefaultProperties(props);
        } else {
            log.info("No 'fintech.webhook.dummy.stub.config.file.path' property found, starting with default on classpath");
        }
        return application.sources(FintechWebhookDummyStubApplication.class);
    }

}
