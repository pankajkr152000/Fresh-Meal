package com.foodies.freshmeal.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.foodies.freshmeal.common.constants.AppConstants;


@Configuration
public class CorsConfigToConnectAdminPanel {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(
                    CorsRegistry registry) {

                registry.addMapping("/**")
                        .allowedOrigins(AppConstants.ADMINPANEL_URL) // admin_url http://localhost:5173
                        .allowedMethods("*");
            }
        };
    }
}
