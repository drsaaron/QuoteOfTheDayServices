/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.rest.config.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

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
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper mapper = builder
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        mapper.setDateFormat(new SimpleDateFormat(dateFormat));
        return mapper;
    }
}
