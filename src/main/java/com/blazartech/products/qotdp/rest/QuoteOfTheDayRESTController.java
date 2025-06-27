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
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * @author scott
 */
@RestController
@CrossOrigin
@OpenAPIDefinition(info = @Info(
        title = "data access services for the quote of the day application",
        version = "1.0"
))
public class QuoteOfTheDayRESTController {

    private static final Logger logger = LoggerFactory.getLogger(QuoteOfTheDayRESTController.class);

    @Autowired
    private QuoteOfTheDayDAL dal;

    @Autowired
    private GetQuoteOfTheDayPAB getQuoteOfTheDayPAB;

    @Value("${dateServices.date.format}")
    private String dateFormat;

    /*
     **************************************
     Quotes
     **************************************
     */
    @RequestMapping(value = "/quote", method = RequestMethod.GET)
    @Operation(summary = "get a list of all the available quotes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "list of quotes",
                content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Quote.class))
                    )
                })
    })
    @PreAuthorize("hasAuthority('ROLE_QUOTE_OF_THE_DAY_USER')")
    public Collection<Quote> getQuotes() {
        logger.info("getting all quotes");

        return dal.getAllQuotes();
    }

    @RequestMapping(value = "/quote", method = RequestMethod.GET, params = "sourceCode")
    @Operation(summary = "get a list of all the available quotes for a source code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "list of quotes",
                content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Quote.class))
                    )
                })
    })
    @PreAuthorize("hasAuthority('ROLE_QUOTE_OF_THE_DAY_USER')")
    public Collection<Quote> getQuotesForSourceCode(@Parameter(description = "a source code") @RequestParam int sourceCode) {
        logger.info("getting quotes for source code " + sourceCode);

        return dal.getQuotesForSourceCode(sourceCode);
    }

    @RequestMapping(value = "/quote/{id}", method = RequestMethod.GET)
    @Operation(summary = "get a specific quotes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "quote",
                content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Quote.class)
                    )
                })
    })    
    @PreAuthorize("hasAuthority('ROLE_QUOTE_OF_THE_DAY_USER')")
    public Quote getQuote(@Parameter(description = "quote ID") @PathVariable int id) {
        logger.info("getting quote #" + id);

        return dal.getQuote(id);
    }

    @RequestMapping(value = "/quote", method = RequestMethod.POST)
    @Transactional
    @Operation(summary = "add a new quote")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "new quote",
                content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Quote.class)
                    )
                })
    })
    @PreAuthorize("hasAuthority('ROLE_QOTD_ADMIN_USER')")
    public Quote addQuote(@Parameter(description = "new quote data") @RequestBody Quote quote) {
        logger.info("adding quote " + quote.getText());

        dal.addQuote(quote);
        return quote;
    }

    @RequestMapping(value = "/quote/{id}", method = RequestMethod.PUT)
    @Transactional
    @Operation(summary = "update a quote")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "updated quote",
                content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Quote.class)
                    )
                })
    })
    @PreAuthorize("hasAuthority('ROLE_QOTD_ADMIN_USER')")
    public Quote updateQuote(
            @Parameter(description = "quote ID") @PathVariable int id, 
            @Parameter(description = "updated quote data") @RequestBody Quote quote) {
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
    @Operation(summary = "get the quote of the day history for a quote")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "quote of the day history",
                content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuoteOfTheDayHistory.class)
                    )
                })
    })
    @PreAuthorize("hasAuthority('ROLE_QUOTE_OF_THE_DAY_USER')")
    public QuoteOfTheDayHistory getQuoteOfTheDayHistoryForQuote(@Parameter(description = "quote ID") @PathVariable int id) {
        logger.info("getting quote of the day history for " + id);

        return dal.getQuoteOfTheDayHistoryForQuote(id);
    }

    /*
     **************************************
     Quotes of the day
     **************************************
     */
    @RequestMapping(value = "/qotd", method = RequestMethod.GET)
    @Operation(summary = "get the quote of the day for the current date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "quote of the day",
                content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuoteOfTheDay.class)
                    )
                })
    })
    @PreAuthorize("hasAuthority('ROLE_QUOTE_OF_THE_DAY_USER')")
    public QuoteOfTheDay getQuoteOfTheDay() {
        logger.info("getting quote of the day for today");

        return getQuoteOfTheDayPAB.getQuoteOfTheDay();
    }

    @RequestMapping(value = "/qotd/{runDate}", method = RequestMethod.GET)
    @Operation(summary = "get the quote of the day for a specific date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "quote of the day",
                content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Quote.class)
                    )
                })
    })   
    @PreAuthorize("hasAuthority('ROLE_QUOTE_OF_THE_DAY_USER')")
    public QuoteOfTheDay getQuoteOfTheDay(@Parameter(description = "run date, format yyyy-MM-dd") @PathVariable LocalDate runDate) {
        logger.info("getting quote of the day for " + runDate);
        return getQuoteOfTheDayPAB.getQuoteOfTheDay(runDate);
    }

    @RequestMapping(value = "/qotd", method = RequestMethod.POST)
    @Transactional
    @Operation(summary = "add a quote of the day")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "new quote of the day",
                content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuoteOfTheDay.class)
                    )
                })
    })
    @PreAuthorize("hasAuthority('ROLE_QOTD_ADMIN_USER')")
    public QuoteOfTheDay addQuoteOfTheDay(@Parameter(description = "quote of the day") @RequestBody QuoteOfTheDay qotd) {
        logger.info("adding quote " + qotd.getQuoteNumber() + " as quote of the day for " + qotd.getRunDate());

        dal.addQuoteOfTheDay(qotd);

        return qotd;
    }

    /*
     **************************************
     source codes
     **************************************
     */
    @RequestMapping(value = "/sourceCode", method = RequestMethod.GET)
    @Operation(summary = "get a list of all the available quote source codes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "list of quote source codes",
                content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = QuoteSourceCode.class))
                    )
                })
    })
    @PreAuthorize("hasAuthority('ROLE_QUOTE_OF_THE_DAY_USER')")
    public Collection<QuoteSourceCode> getQuoteSourceCodes() {
        logger.info("getting all quote source codes");

        return dal.getQuoteSourceCodes();
    }

    @RequestMapping(value = "/sourceCode/{id}", method = RequestMethod.GET)
    @Operation(summary = "get a specific quote source code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "quote source code",
                content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuoteSourceCode.class)
                    )
                })
    })
    @PreAuthorize("hasAuthority('ROLE_QUOTE_OF_THE_DAY_USER')")
    public QuoteSourceCode getQuoteSourceCode(@Parameter(description = "source code") @PathVariable int id) {
        logger.info("getting source code " + id);

        return dal.getQuoteSourceCode(id);
    }

    @PostMapping(value = "/sourceCode")
    @Transactional
    @Operation(summary = "add a new source code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "new source code",
                content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuoteSourceCode.class)
                    )
                })
    })
    @PreAuthorize("hasAuthority('ROLE_QOTD_ADMIN_USER')")
    public QuoteSourceCode addSourceCode(@Parameter(description = "source coce") @RequestBody QuoteSourceCode sourceCode) {
        logger.info("adding source code " + sourceCode.getText());
        dal.addQuoteSourceCode(sourceCode);
        return sourceCode;
    }

    @RequestMapping(value = "/sourceCode/{id}", method = RequestMethod.PUT)
    @Transactional
    @Operation(summary = "update a quote source code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "updated source code",
                content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuoteSourceCode.class)
                    )
                })
    })
    @PreAuthorize("hasAuthority('ROLE_QOTD_ADMIN_USER')")
    public QuoteSourceCode updateSourceCode(
            @Parameter(description = "source code") @PathVariable int id,
            @Parameter(description = "updated source code data") @RequestBody QuoteSourceCode sourceCode) {
        logger.info("updating source code " + id);
        dal.updateQuoteSourceCode(sourceCode);
        return sourceCode;
    }
}
