package com.jmpm.microservice.service.impl;

import org.json.JSONObject;
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

	private static final String URI_FRANKFURTER_API = "https://api.frankfurter.app/latest";
	private static final String URI_EXCHANGERATES_API = "https://api.exchangeratesapi.io/latest";
	private static final String URI_RATES_API = "https://api.ratesapi.io/api/latest";
    
	@Override
	public Double convertEurToUsd(double amount) {
		
		try {
			ResponseEntity<String> frankfurterRateResp = getRate(URI_FRANKFURTER_API);
			ResponseEntity<String> exchangeRatesResp = getRate(URI_EXCHANGERATES_API);
			ResponseEntity<String> ratesRateResp = getRate(URI_RATES_API);
		
			Double frankfurterApiUsdRate = extractUSDRate(frankfurterRateResp.getBody());
			Double exchangeApiUsdRate = extractUSDRate(exchangeRatesResp.getBody());
			Double ratesApiUsdRate = extractUSDRate(ratesRateResp.getBody());
			
			return amount * Math.max(frankfurterApiUsdRate, Math.max(exchangeApiUsdRate, ratesApiUsdRate));
		
		} catch (Exception e) {
			log.info("Error to extract USD rate");
			return null;
		}
	}
	
	private ResponseEntity<String> getRate(final String uri) {
		
		log.info("Find rates in " +uri);
		
		RestTemplate restTemplate = new RestTemplate();
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
	    HttpEntity<?> request = new HttpEntity<>(headers);
	    
	    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
	    log.info(result.getStatusCode().toString());
	    log.info(result.getBody());
	    
	    return result;
	}

	private Double extractUSDRate(final String rates) {
		
		JSONObject jsonResp = new JSONObject(rates);
		JSONObject jsonRates = (JSONObject) jsonResp.get("rates");
		Double dRate = (double) jsonRates.get("USD");
		log.info(dRate.toString());
	    
	    return dRate;
	}

}