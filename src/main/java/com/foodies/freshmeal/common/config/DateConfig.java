package com.foodies.freshmeal.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DateConfig {

    @Value("${app.date.format}")
    private String dateFormat;

    public String getDateFormat() {
        return dateFormat;
    }
}
