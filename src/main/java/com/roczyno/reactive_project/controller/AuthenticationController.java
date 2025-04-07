package com.roczyno.reactive_project.controller;

import com.roczyno.reactive_project.dto.AuthenticationRequest;
import com.roczyno.reactive_project.service.AuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	private final AuthenticationService authenticationService;

	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping("/login")
	public Mono<ResponseEntity<Object>> login(@RequestBody Mono<AuthenticationRequest> request){
		return request
				.flatMap(authenticationRequest->authenticationService.authenticate(authenticationRequest.email(),authenticationRequest.password()))
				.map( authenticationResultMap->ResponseEntity.ok()
						.header(HttpHeaders.AUTHORIZATION,"Bearer "+authenticationResultMap.get("token"))
						.header("userId",authenticationResultMap.get("userId")).build());
//				.onErrorReturn(BadCredentialsException.class,ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"))
//				.onErrorReturn(Exception.class,ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}
}
