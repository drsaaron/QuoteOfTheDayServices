/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.rest;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 *
 * @author scott
 */
@SpringBootApplication
@ComponentScan("com.blazartech")
public class Main {
    
    private static final Logger logger = Logger.getLogger(Main.class);
    
    public static void main(String args[]) {
        ApplicationContext context = SpringApplication.run(Main.class, args);
        logger.info("REST services started.");
    }
}
