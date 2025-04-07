package com.roczyno.reactive_project.service;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface AuthenticationService {
	Mono<Map<String,String>> authenticate(String email, String password);
}
