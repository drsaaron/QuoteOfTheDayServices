/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.rest.config.response;

import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * A configuration class to setup the jackson object mapper to map dates with the 
 * desired date format.  We could use @JsonFormat everywhere a date is used, but
 * then the format is hard-coded in many places.  With this approach, the date format
 * can be set in a properties file and brought in via spring.
 * 
 * @author aar1069
 */
@Configuration
public class DateFormatterConfiguration {

    @Value("${dateServices.date.format}")
    private String dateFormat;

    @Bean
    public ObjectMapper objectMapper() {
        JsonMapper mapper = JsonMapper.builder()
                .defaultDateFormat(new SimpleDateFormat(dateFormat))
                .build();
        return mapper;
    }
}
