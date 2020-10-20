package com.jmpm.microservice.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jmpm.microservice.service.MyService;

@RestController
public class MyController {

	@Autowired
	private MyService service;

	@RequestMapping("/convert/eur/usd/{amount}")
	public @ResponseBody double convertEurToUsd(@PathVariable("amount") double amount) {
		return service.convertEurToUsd(amount);
	}

}
