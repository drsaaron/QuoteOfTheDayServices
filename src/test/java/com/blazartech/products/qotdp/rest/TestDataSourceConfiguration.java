/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.products.qotdp.rest;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 *
 * @author scott
 */
@Configuration
public class TestDataSourceConfiguration {
    
    @Value("${com.blazartech.products.qotdp.data.access.impl.spring.ds.userId}")
    private String userID;
    
    @Value("${com.blazartech.products.qotdp.data.access.impl.spring.ds.url}")
    private String url;
    
    @Value("${com.blazartech.products.qotdp.data.access.impl.spring.ds.driverClass}")
    private String driverClass;
    
    private final int poolSize = 5;
    
    @Bean(name = "quoteOfTheDataDataSource", destroyMethod = "")
    @Primary
    public DataSource getDataSource() {
        
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driverClass);
        ds.setUrl(url);
        ds.setUsername(userID);
        ds.setPassword("yo");
        ds.setInitialSize(poolSize);
        ds.setMaxTotal(poolSize);
        
        return ds;
    }
}
