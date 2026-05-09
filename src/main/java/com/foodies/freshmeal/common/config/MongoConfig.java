package com.foodies.freshmeal.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;




/**
 * =====================================================
 * Mongo Configuration
 * =====================================================
 *
 * Registers:
 *
 * - Mongo Client
 * - Mongo Logging Listener
 *
 * =====================================================
 */
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Bean
    @SuppressWarnings("unused")
    MongoClient mongoClient() {

        MongoClientSettings settings =
                MongoClientSettings.builder()
                        .applyConnectionString(
                                new ConnectionString(
                                        mongoUri))
                        .addCommandListener(
                                new MongoLoggingListener())
                        .build();

        return MongoClients.create(settings);
    }
}