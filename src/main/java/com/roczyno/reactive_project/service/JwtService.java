package com.roczyno.reactive_project.service;

import reactor.core.publisher.Mono;

public interface JwtService {
	String generateJwt(String subject);
	Mono<Boolean> validateJwt(String token);
	String extractTokenSubject(String token);
}
