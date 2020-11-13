package com.jmpm.microservice.service;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.WireMockServer;


@SpringBootTest
public class MyServiceTest {
	
    private static final String frankfurterApiUrl = "/api.frankfurter.app/latest";
    private static final String exchangeRatesapiUrl = "/api.exchangeratesapi.io/latest";
    private static final String ratesApiUrl = "/api.ratesapi.io/api/latest";


	@Autowired
    private MyService myService;
    
    private WireMockServer wireMockServer;
    
    @BeforeEach
    public void setup () {

        wireMockServer = new WireMockServer(8989);

        wireMockServer.start();
        
        wireMockServer.stubFor(get(urlEqualTo(frankfurterApiUrl))
            .willReturn(aResponse().withHeader("Content-Type", "text/plain")
		        .withStatus(200)
		        .withBodyFile("json/frankfurter.json")));

        wireMockServer.stubFor(get(urlEqualTo(exchangeRatesapiUrl))
            .willReturn(aResponse().withHeader("Content-Type", "text/plain")
	            .withStatus(200)
	            .withBodyFile("json/ratesapi.json")));
        
        wireMockServer.stubFor(get(urlEqualTo(ratesApiUrl))
            .willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBodyFile("json/exchangeratesapi.json")));  
        
    }
    
    @AfterEach
    public void teardown () {
        wireMockServer.stop();
    }


//    @Test
//    void testLocalServiceWithMockedRemoteService(){
//    	String amount = "1";
//    	RestTemplate restTemplate = new RestTemplate();
//    	ResponseEntity<BigDecimal> response = restTemplate.getForEntity("http://localhost:8080/convert/eur/usd/{amount}", BigDecimal.class, amount);
//    	assertNotNull(response.getBody());
//    	verify(exactly(1),getRequestedFor(urlEqualTo(frankfurterApiUrl)));
//    	verify(exactly(1),getRequestedFor(urlEqualTo(exchangeRatesapiUrl)));
//		verify(exactly(1),getRequestedFor(urlEqualTo(ratesApiUrl)));
//    }
    
    /**
     * Test exchange api find max rate.
     */
    @Test
    void given_exchangeApiURL_findMax_rate() {
    	String result = myService.convertEurToUsd("1");
        assertEquals("1.1791", result);
//    	verify(exactly(1),getRequestedFor(urlEqualTo(frankfurterApiUrl)));
//    	verify(exactly(1),getRequestedFor(urlEqualTo(exchangeRatesapiUrl)));
//		verify(exactly(1),getRequestedFor(urlEqualTo(ratesApiUrl)));
    }
}
