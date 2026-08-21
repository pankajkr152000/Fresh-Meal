package com.foodies.freshmeal.pincode.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * =============================================================================
 * Configuration : PincodeApiConfig
 * =============================================================================
 *
 * Purpose
 * -------
 * Configures the RestClient used to communicate with the external pincode API.
 *
 * The base URL is read from application.properties so that the external API
 * configuration remains outside the Java code.
 * =============================================================================
 */
@Configuration
public class PincodeApiConfig {

    @Bean
    public RestClient pincodeRestClient(
            @Value("${pincode.api.base-url}") String baseUrl) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}