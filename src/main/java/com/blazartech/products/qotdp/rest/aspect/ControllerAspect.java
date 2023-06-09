/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.products.qotdp.rest.aspect;

import java.util.Date;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 *
 * @author scott
 */
@Aspect
@Component
public class ControllerAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);
    
    @Pointcut("execution(* com.blazartech.products.qotdp.rest.QuoteOfTheDayRESTController.getQuoteOfTheDay(java.util.Date)) && args(runDate)")
    public void trackGetQuoteOfTheDayForDate(Date runDate) {
        logger.info("getting quote of the day");
    }
    
    @Pointcut("execution(* com.blazartech.products.qotdp.rest.QuoteOfTheDayRESTController.getQuoteOfTheDay())")
    public void trackGetQuoteOfTheDayNoDate() {
        logger.info("getting quote of the day");
    }
    
    @Before("trackGetQuoteOfTheDayForDate(runDate)")
    public void doBeforeForDate(JoinPoint call, Date runDate) {
        logger.info("getting QOTD for run date {}", runDate);
    }
    
    private Object profileCall(ProceedingJoinPoint call) throws Throwable {
        StopWatch clock = new StopWatch("Profiling for '" + call.toShortString());
        try {
            clock.start(call.toShortString());
            return call.proceed();
        } finally {
            clock.stop();
            logger.info(clock.prettyPrint());
        }
    }
    
    @Around("trackGetQuoteOfTheDayForDate(runDate)")
    public Object profileGetQuoteOfTheDayForDate(ProceedingJoinPoint call, Date runDate) throws Throwable {
        return profileCall(call);
    }
    
    @Before("trackGetQuoteOfTheDayNoDate()")
    public void doBeforeNoDate(JoinPoint call) {
        logger.info("getting QOTD for run date today");
    }
    
    @Around("trackGetQuoteOfTheDayNoDate()")
    public Object profileGetQuoteOfTheDayNoDate(ProceedingJoinPoint call) throws Throwable {
        return profileCall(call);
    }
}
