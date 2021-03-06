package com.udacity.pricing;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.List;

/**
 *  Integration tests for the pricing service
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PricingServiceIntegrationTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    // Test that the Pricing Service will return the correct response when all prices are requested
    @Test
    public void getAllPrices() {
        // Get all prices
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/prices/", String.class);

        // Verify that the OK response is received
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
    }

    // Test that the Pricing Service will return the correct response when a price by id is requested
    @Test
    public void getPriceById() {
        // Get price 5
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/prices/5", String.class);

        // Verify that the OK response is received
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
    }
}
