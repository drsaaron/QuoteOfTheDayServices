/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.blazartech.products.qotdp.rest;

import com.blazartech.products.qotdp.data.Quote;
import com.blazartech.products.qotdp.data.QuoteOfTheDay;
import com.blazartech.products.qotdp.data.QuoteOfTheDayHistory;
import com.blazartech.products.qotdp.data.QuoteSourceCode;
import com.blazartech.products.qotdp.data.access.QuoteOfTheDayDAL;
import com.blazartech.products.qotdp.data.access.impl.spring.jpa.QuoteOfTheDayDALSpringJpaImpl;
import com.blazartech.products.qotdp.data.access.impl.spring.jpa.SourceCodeComparatorConfiguration;
import com.blazartech.products.qotdp.data.access.impl.spring.jpa.config.EntityManagerConfig;
import com.blazartech.products.qotdp.data.access.impl.spring.jpa.config.JpaVendorAdapterConfig;
import com.blazartech.products.qotdp.data.access.impl.spring.jpa.config.TransactionManagerConfig;
import com.blazartech.products.qotdp.process.GetQuoteOfTheDayPAB;
import com.blazartech.products.qotdp.process.impl.GetQuoteOfTheDayPABImpl;
import com.blazartech.products.qotdp.process.impl.PriorDateDetermination;
import com.blazartech.products.qotdp.process.impl.PriorDateDeterminationImpl;
import com.blazartech.products.qotdp.process.impl.RandomIndexGenerator;
import com.blazartech.products.qotdp.process.impl.RandomIndexGeneratorImpl;
import com.blazartech.products.services.date.DateServices;
import com.blazartech.products.services.date.impl.DateServicesImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author scott
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(QuoteOfTheDayRESTController.class)
@ContextConfiguration(classes = {
    QuoteOfTheDayRESTControllerTest.QuoteOfTheDayRESTControllerTestConfiguration.class,
    TestDataSourceConfiguration.class,
    EntityManagerConfig.class,
    JpaVendorAdapterConfig.class,
    TransactionManagerConfig.class,
    SourceCodeComparatorConfiguration.class
})
@Transactional
public class QuoteOfTheDayRESTControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(QuoteOfTheDayRESTControllerTest.class);

    @Configuration
    @PropertySource("classpath:unittest.properties")
    public static class QuoteOfTheDayRESTControllerTestConfiguration {

        @Bean
        public QuoteOfTheDayRESTController instance() {
            return new QuoteOfTheDayRESTController();
        }
        
        @Bean
        public QuoteOfTheDayDAL dal() {
            return new QuoteOfTheDayDALSpringJpaImpl();
        }
        
        @Bean
        public GetQuoteOfTheDayPAB quoteOfTheDayPAB() {
            return new GetQuoteOfTheDayPABImpl();
        }
        
        @Bean
        public DateServices dateServices() {
            return new DateServicesImpl();
        }
        
        @Bean
        public RandomIndexGenerator randomIndexGenerator() {
            return new RandomIndexGeneratorImpl();
        }
        
        @Bean
        public PriorDateDetermination priorDateDetermination() {
            return new PriorDateDeterminationImpl();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    public QuoteOfTheDayRESTControllerTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test of getQuotes method, of class QuoteOfTheDayRESTController.
     */
    @Test
    @Sql("classpath:testQuotes.sql")
    public void testGetQuotes() {
        logger.info("getQuotes");

        try {
            MvcResult result = mockMvc
                    .perform(
                            get("/quote")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            String response = result.getResponse().getContentAsString();
            Quote[] quotes = objectMapper.readValue(response, Quote[].class);

            assertNotNull(quotes);
            assertEquals(4, quotes.length);

        } catch (Exception e) {
            throw new RuntimeException("error running test: " + e.getMessage(), e);
        }
    }

    /**
     * Test of getQuotesForSourceCode method, of class
     * QuoteOfTheDayRESTController.
     */
    @Test
    @Sql("classpath:testQuotes.sql")
    public void testGetQuotesForSourceCode() {
        logger.info("getQuotesForSourceCode");

        int sourceCode = 1;
        try {
            MvcResult result = mockMvc
                    .perform(
                            get("/quote?sourceCode=" + sourceCode)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            String response = result.getResponse().getContentAsString();
            Quote[] quotes = objectMapper.readValue(response, Quote[].class);

            assertNotNull(quotes);
            assertEquals(2, quotes.length);

        } catch (Exception e) {
            throw new RuntimeException("error running test: " + e.getMessage(), e);
        }
    }

    /**
     * Test of getQuote method, of class QuoteOfTheDayRESTController.
     */
    @Test
    @Sql("classpath:testQuotes.sql")
    public void testGetQuote() {
        logger.info("getQuotesForSourceCode");

        int quoteNumber = 1;
        try {
            MvcResult result = mockMvc
                    .perform(
                            get("/quote/" + quoteNumber)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            String response = result.getResponse().getContentAsString();
            Quote quote = objectMapper.readValue(response, Quote.class);

            assertNotNull(quote);
            assertEquals("I am a quote", quote.getText());

        } catch (Exception e) {
            throw new RuntimeException("error running test: " + e.getMessage(), e);
        }
    }

    /**
     * Test of addQuote method, of class QuoteOfTheDayRESTController.
     */
    //@Test
    public void testAddQuote() {
        System.out.println("addQuote");
        Quote quote = null;
        QuoteOfTheDayRESTController instance = new QuoteOfTheDayRESTController();
        Quote expResult = null;
        Quote result = instance.addQuote(quote);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateQuote method, of class QuoteOfTheDayRESTController.
     */
    //@Test
    public void testUpdateQuote() {
        System.out.println("updateQuote");
        int id = 0;
        Quote quote = null;
        QuoteOfTheDayRESTController instance = new QuoteOfTheDayRESTController();
        Quote expResult = null;
        Quote result = instance.updateQuote(id, quote);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getQuoteOfTheDayHistoryForQuote method, of class
     * QuoteOfTheDayRESTController.
     */
    //@Test
    public void testGetQuoteOfTheDayHistoryForQuote() {
        System.out.println("getQuoteOfTheDayHistoryForQuote");
        int id = 0;
        QuoteOfTheDayRESTController instance = new QuoteOfTheDayRESTController();
        QuoteOfTheDayHistory expResult = null;
        QuoteOfTheDayHistory result = instance.getQuoteOfTheDayHistoryForQuote(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getQuoteOfTheDay method, of class QuoteOfTheDayRESTController.
     */
    //@Test
    public void testGetQuoteOfTheDay_0args() {
        System.out.println("getQuoteOfTheDay");
        QuoteOfTheDayRESTController instance = new QuoteOfTheDayRESTController();
        QuoteOfTheDay expResult = null;
        QuoteOfTheDay result = instance.getQuoteOfTheDay();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getQuoteOfTheDay method, of class QuoteOfTheDayRESTController.
     */
    //@Test
    public void testGetQuoteOfTheDay_Date() {
        System.out.println("getQuoteOfTheDay");
        Date runDate = null;
        QuoteOfTheDayRESTController instance = new QuoteOfTheDayRESTController();
        QuoteOfTheDay expResult = null;
        QuoteOfTheDay result = instance.getQuoteOfTheDay(runDate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addQuoteOfTheDay method, of class QuoteOfTheDayRESTController.
     */
    //@Test
    public void testAddQuoteOfTheDay() {
        System.out.println("addQuoteOfTheDay");
        QuoteOfTheDay qotd = null;
        QuoteOfTheDayRESTController instance = new QuoteOfTheDayRESTController();
        QuoteOfTheDay expResult = null;
        QuoteOfTheDay result = instance.addQuoteOfTheDay(qotd);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getQuoteSourceCodes method, of class QuoteOfTheDayRESTController.
     */
    //@Test
    public void testGetQuoteSourceCodes() {
        System.out.println("getQuoteSourceCodes");
        QuoteOfTheDayRESTController instance = new QuoteOfTheDayRESTController();
        Collection<QuoteSourceCode> expResult = null;
        Collection<QuoteSourceCode> result = instance.getQuoteSourceCodes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getQuoteSourceCode method, of class QuoteOfTheDayRESTController.
     */
    //@Test
    public void testGetQuoteSourceCode() {
        System.out.println("getQuoteSourceCode");
        int id = 0;
        QuoteOfTheDayRESTController instance = new QuoteOfTheDayRESTController();
        QuoteSourceCode expResult = null;
        QuoteSourceCode result = instance.getQuoteSourceCode(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addSourceCode method, of class QuoteOfTheDayRESTController.
     */
    //@Test
    public void testAddSourceCode() {
        System.out.println("addSourceCode");
        QuoteSourceCode sourceCode = null;
        QuoteOfTheDayRESTController instance = new QuoteOfTheDayRESTController();
        QuoteSourceCode expResult = null;
        QuoteSourceCode result = instance.addSourceCode(sourceCode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateSourceCode method, of class QuoteOfTheDayRESTController.
     */
    //@Test
    public void testUpdateSourceCode() {
        System.out.println("updateSourceCode");
        int id = 0;
        QuoteSourceCode sourceCode = null;
        QuoteOfTheDayRESTController instance = new QuoteOfTheDayRESTController();
        QuoteSourceCode expResult = null;
        QuoteSourceCode result = instance.updateSourceCode(id, sourceCode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
