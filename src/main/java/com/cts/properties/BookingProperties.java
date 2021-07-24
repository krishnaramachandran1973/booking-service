package com.cts.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "booking")
public class BookingProperties {
	
	private String fareUrl;
	private String amqpSearchQueue;

}
