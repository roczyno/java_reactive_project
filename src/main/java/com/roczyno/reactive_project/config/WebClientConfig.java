package com.roczyno.reactive_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Configuration
public class WebClientConfig {

	@Bean
	public WebClient webClient(){
		return WebClient.builder()
				.baseUrl("http://localhost:8080")
				.defaultHeaders(headers->{
					headers.setContentType(MediaType.APPLICATION_JSON);
					headers.setAccept(List.of(MediaType.APPLICATION_JSON));
				})
				.build();
	}
}
