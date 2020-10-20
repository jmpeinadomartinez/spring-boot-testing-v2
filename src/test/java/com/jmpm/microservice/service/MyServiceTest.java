package com.jmpm.microservice.service;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.jmpm.microservice.SpringBootTestingV2Application;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = SpringBootTestingV2Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MyServiceTest {
	
    private final static String frankfurterApiUrl = "https://api.frankfurter.app/latest";
    private final static String exchangeRatesapiUrl = "https://api.exchangeratesapi.io/latest";
    private final static String ratesApiUrl = "https://api.ratesapi.io/api/latest";
    
	@Rule
//	public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().httpsPort(8443));
//	public WireMockRule wireMockRule = new WireMockRule(9999);
	public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().httpsPort(8443).httpDisabled(true));
//	public WireMockRule wireMockRule = new WireMockRule(options().dynamicHttpsPort());	
	
    @Before
    public void setup () {

        stubFor(get(urlEqualTo(frankfurterApiUrl))
            .willReturn(aResponse().withHeader("Content-Type", "text/plain")
		        .withStatus(200)
		        .withBodyFile("json/frankfurter.json")));

        stubFor(get(urlEqualTo(exchangeRatesapiUrl))
            .willReturn(aResponse().withHeader("Content-Type", "text/plain")
	            .withStatus(200)
	            .withBodyFile("json/ratesapi.json")));
        
        stubFor(get(urlEqualTo(ratesApiUrl))
            .willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBodyFile("json/exchangeratesapi.json")));  

        
    }

    @Test
    public void testLocalServiceWithMockedRemoteService() throws Exception {
    	RestTemplate restTemplate = new RestTemplate();
    	ResponseEntity<Double> response = restTemplate.getForEntity("http://localhost:8080/convert/eur/usd/1000", Double.class);
    	assertNotNull(response.getBody());
    	System.out.println(response.getBody());
    
    	verify(exactly(1),getRequestedFor(urlEqualTo(frankfurterApiUrl)));
    	verify(exactly(1),getRequestedFor(urlEqualTo(exchangeRatesapiUrl)));
		verify(exactly(1),getRequestedFor(urlEqualTo(ratesApiUrl)));
    }
}



























//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.github.tomakehurst.wiremock.WireMockServer;
//import com.github.tomakehurst.wiremock.client.WireMock;
//
//import static com.github.tomakehurst.wiremock.client.WireMock.*;
//import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
//
//
//@SpringBootTest
//public class MyServiceTest {
//	
//	private static final String URI_FRANKFURTER_API = "https://api.frankfurter.app/latest";
//	private static final String URI_EXCHANGERATES_API = "https://api.exchangeratesapi.io/latest";
//	private static final String URI_RATES_API = "https://api.ratesapi.io/api/latest";
//	
//	@Autowired
//	private MyService service;
//	
//	private WireMockServer wireMockServer;
//	
//    @BeforeEach
//    public void setup () {
//    	
//        wireMockServer = new WireMockServer(options().port(8080));
//        wireMockServer.start();
//
//        //Normally we don't have to do this if we use the default configuration.
//        //However, because other tests make changes to the global WireMock configuration,
//        //we have to make the same changes here because we don't know the invocation
//        //order of our test classes.
//        configureFor("localhost", 8080);
//
//        wireMockServer.stubFor(get(urlEqualTo(URI_FRANKFURTER_API))
//            .willReturn(aResponse().withHeader("Content-Type", "text/plain")
//		        .withStatus(200)
//		        .withBodyFile("json/frankfurter.json")));
//
//        wireMockServer.stubFor(get(urlEqualTo(URI_EXCHANGERATES_API))
//            .willReturn(aResponse().withHeader("Content-Type", "text/plain")
//	            .withStatus(200)
//	            .withBodyFile("json/ratesapi.json")));
//        
//        wireMockServer.stubFor(get(urlEqualTo(URI_RATES_API))
//            .willReturn(aResponse().withHeader("Content-Type", "application/json")
//                .withStatus(200)
//                .withBodyFile("json/exchangeratesapi.json")));  
//
//        
//    }	
//    	
//    @AfterEach
//    public void stopWireMockServer() {
//        wireMockServer.stop();
//    }
//	
//	
//	@Test
//	public void get_best_rate_for_usd(){
//		
//		double amount = 1.5D;
//		
//		Double exchange = service.convertEurToUsd(amount);
//		assertNotNull(exchange);
//		
//		wireMockServer.verify(WireMock.exactly(0),WireMock.getRequestedFor(urlEqualTo(URI_FRANKFURTER_API)));
//		wireMockServer.verify(WireMock.exactly(0),WireMock.getRequestedFor(urlEqualTo(URI_EXCHANGERATES_API)));
//		wireMockServer.verify(WireMock.exactly(0),WireMock.getRequestedFor(urlEqualTo(URI_RATES_API)));
//	}
//}
