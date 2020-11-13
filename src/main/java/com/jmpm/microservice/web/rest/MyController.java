package com.jmpm.microservice.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jmpm.microservice.service.MyService;

@RestController
public class MyController {

	@Autowired
	private MyService service;

//	@RequestMapping("/convert/eur/usd/{amount}")
	@GetMapping("/convert/eur/usd/{amount}")
	public @ResponseBody String convertEurToUsd(@PathVariable("amount") String amount) {
		return service.convertEurToUsd(amount);
	}

}
