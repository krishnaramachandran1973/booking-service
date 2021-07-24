package com.cts.configuration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.vo.Fare;

@FeignClient(name = "api-gateway", path = "/fare")
public interface FareServiceProxy {

	@GetMapping
	Fare getFare(@RequestParam("flightNumber") String flightNumber, @RequestParam("flightDate") String flightDate);
}
