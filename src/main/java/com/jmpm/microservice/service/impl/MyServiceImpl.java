package com.jmpm.microservice.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
	
    private final static String frankfurterApiUrl = "https://api.frankfurter.app/latest";
    private final static String exchangeRatesapiUrl = "https://api.exchangeratesapi.io/latest";
    private final static String ratesApiUrl = "https://api.ratesapi.io/api/latest";
	
	@Autowired
	private RestTemplate restTemplate;
    
	@Override
	public Double convertEurToUsd(double amount) {
		
		try {
			
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		    HttpEntity<?> request = new HttpEntity<>(headers);
			
		    ResponseEntity<String> response1 = restTemplate.getForEntity(frankfurterApiUrl, String.class);
		    ResponseEntity<String> response2 = restTemplate.getForEntity(exchangeRatesapiUrl, String.class);
		    ResponseEntity<String> response3 = restTemplate.exchange(ratesApiUrl, HttpMethod.GET, request, String.class);
			
			Double frankfurterApiUsdRate = extractUSDRate(response1.getBody());
			Double exchangeApiUsdRate = extractUSDRate(response2.getBody());
			Double ratesApiUsdRate = extractUSDRate(response3.getBody());

			return amount * Math.max(frankfurterApiUsdRate, Math.max(exchangeApiUsdRate, ratesApiUsdRate));
					
		} catch (Exception e) {
			log.info("Error to extract USD rate");
			return null;
		}
	}

	private Double extractUSDRate(final String rates) {
		
		JSONObject jsonResp = new JSONObject(rates);
		JSONObject jsonRates = (JSONObject) jsonResp.get("rates");
		Double dRate = (double) jsonRates.get("USD");
		log.info(dRate.toString());
	    
	    return dRate;
	}

}