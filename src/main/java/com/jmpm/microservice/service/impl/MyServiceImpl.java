package com.jmpm.microservice.service.impl;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jmpm.microservice.service.MyService;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class MyServiceImpl implements MyService {
	
	@Value("${api.frankfurter}")
	private String frankfurterApiUrl;
	
	@Value("${api.ratesapi}")
	private String exchangeRatesapiUrl;
	
	@Value("${api.exchangeratesapi}")
	private String ratesApiUrl;  
	
	@Autowired
	private RestTemplate restTemplate;
    
	@Override
	public String convertEurToUsd(String amount) {
		
		try {
			
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		    HttpEntity<?> request = new HttpEntity<>(headers);
			
		    ResponseEntity<String> response1 = restTemplate.getForEntity(frankfurterApiUrl, String.class);
		    ResponseEntity<String> response2 = restTemplate.getForEntity(exchangeRatesapiUrl, String.class);
		    ResponseEntity<String> response3 = restTemplate.exchange(ratesApiUrl, HttpMethod.GET, request, String.class);
			
		    BigDecimal frankfurterApiUsdRate = extractUSDRate(response1.getBody());
			BigDecimal exchangeApiUsdRate = extractUSDRate(response2.getBody());
			BigDecimal ratesApiUsdRate = extractUSDRate(response3.getBody());

			return frankfurterApiUsdRate.max(exchangeApiUsdRate.max(ratesApiUsdRate)).multiply(new BigDecimal(amount)).toString();

		} catch (Exception e) {
			log.info("Error to extract USD rate");
			return null;
		}
	}

	private BigDecimal extractUSDRate(final String rates) {
		
		JSONObject jsonResp = new JSONObject(rates);
		JSONObject jsonRates = (JSONObject) jsonResp.get("rates");
		Double dRate = (Double) jsonRates.get("USD");
		log.info(dRate.toString());
	    
	    return BigDecimal.valueOf(dRate);
	}

}