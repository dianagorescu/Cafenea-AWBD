package com.proiect.restaurant.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@TestConfiguration
public class TestConfig {

    @Bean
    public TestRestTemplate testRestTemplate(RestTemplateBuilder builder) {
        RestTemplateBuilder updatedBuilder = builder
            .requestFactory(HttpComponentsClientHttpRequestFactory.class);
        return new TestRestTemplate(updatedBuilder);
    }
}
