/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.rest;

import com.blazartech.products.qotdp.data.Quote;
import com.blazartech.products.qotdp.data.QuoteOfTheDay;
import com.blazartech.products.qotdp.data.QuoteOfTheDayHistory;
import com.blazartech.products.qotdp.data.QuoteSourceCode;
import com.blazartech.products.qotdp.data.access.QuoteOfTheDayDAL;
import com.blazartech.products.qotdp.process.GetQuoteOfTheDayPAB;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author scott
 */
@RestController
public class QuoteOfTheDayRESTController {
    
    private static final Logger logger = LoggerFactory.getLogger(QuoteOfTheDayRESTController.class);

    @Autowired
    private QuoteOfTheDayDAL dal;

    @Autowired
    private GetQuoteOfTheDayPAB getQuoteOfTheDayPAB;
    
    @Value("${dateServices.date.format}")
    private String dateFormat;
    
    /**
     * define a binder that will allow handle dates on requests.  replaces use
     * of @DateTimeFormat
     * 
     * @param binder 
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        df.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, false));
    }
    
    /*
     **************************************
     Quotes
     **************************************
    */
    @RequestMapping(value = "/quote", method = RequestMethod.GET)
    public Collection<Quote> getQuotes() {
        logger.info("getting all quotes");

        return dal.getAllQuotes();
    }
    
    @RequestMapping(value = "/quote", method = RequestMethod.GET, params = "sourceCode")
    public Collection<Quote> getQuotesForSourceCode(@RequestParam int sourceCode) {
        logger.info("getting quotes for source code " + sourceCode);
        
        return dal.getQuotesForSourceCode(sourceCode);
    }

    @RequestMapping(value = "/quote/{id}", method = RequestMethod.GET)
    public Quote getQuote(@PathVariable int id) {
        logger.info("getting quote #" + id);

        return dal.getQuote(id);
    }
    
    @RequestMapping(value = "/quote", method = RequestMethod.POST) 
    @Transactional
    public Quote addQuote(@RequestBody Quote quote) {
        logger.info("adding quote " + quote.getText());
        
        dal.addQuote(quote);
        return quote;
    }
    
    @RequestMapping(value = "/quote/{id}", method = RequestMethod.PUT) 
    @Transactional
    public Quote updateQuote(@PathVariable int id, @RequestBody Quote quote) {
        logger.info("updating quote " + id);
        
        dal.updateQuote(quote);
        return quote;
    }
    
    /*
     ***************************************
     Quote of the day history
     ***************************************
    */
    @RequestMapping(value = "/qotdHistory/{id}", method = RequestMethod.GET)
    public QuoteOfTheDayHistory getQuoteOfTheDayHistoryForQuote(@PathVariable int id) {
        logger.info("getting quote of the day history for " + id);
        
        return dal.getQuoteOfTheDayHistoryForQuote(id);
    }

    /*
     **************************************
     Quotes of the day
     **************************************
    */
    @RequestMapping(value = "/qotd", method = RequestMethod.GET)
    public QuoteOfTheDay getQuoteOfTheDay() {
        logger.info("getting quote of the day for today");
        
        return getQuoteOfTheDayPAB.getQuoteOfTheDay();
    }
    
    @RequestMapping(value = "/qotd/{runDate}", method = RequestMethod.GET)
    public QuoteOfTheDay getQuoteOfTheDay(@PathVariable Date runDate) {
        logger.info("getting quote of the day for " + runDate);
        return getQuoteOfTheDayPAB.getQuoteOfTheDay(runDate);
    }
    
    @RequestMapping(value = "/qotd/{runDate}", params = "quoteNumber", method = RequestMethod.POST)
    @Transactional
    public QuoteOfTheDay addQuoteOfTheDay(@PathVariable Date runDate, @RequestParam int quoteNumber) {
        logger.info("adding quote " + quoteNumber + " as quote of the day for " + runDate);
        
        QuoteOfTheDay qotd = new QuoteOfTheDay();
        qotd.setQuoteNumber(quoteNumber);
        qotd.setRunDate(runDate);
        dal.addQuoteOfTheDay(qotd);
        
        return qotd;
    }

    /*
     **************************************
     source codes
     **************************************
    */
    @RequestMapping(value = "/sourceCode", method = RequestMethod.GET)
    public Collection<QuoteSourceCode> getQuoteSourceCodes() {
        logger.info("getting all quote source codes");

        return dal.getQuoteSourceCodes();
    }
    
    @RequestMapping(value = "/sourceCode/{id}", method = RequestMethod.GET) 
    public QuoteSourceCode getQuoteSourceCode(@PathVariable int id) {
        logger.info("getting source code " + id);
        
        return dal.getQuoteSourceCode(id);
    }
}
