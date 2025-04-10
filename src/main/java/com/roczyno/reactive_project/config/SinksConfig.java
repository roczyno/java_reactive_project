package com.roczyno.reactive_project.config;


import com.roczyno.reactive_project.dto.UserResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class SinksConfig {
	@Bean
	public Sinks.Many<UserResponse> userSink(){
		return Sinks.many().multicast().onBackpressureBuffer();
	}
}
