package com.roczyno.reactive_project.service;

import com.roczyno.reactive_project.dto.CreateUserRequest;
import com.roczyno.reactive_project.dto.UserResponse;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService extends ReactiveUserDetailsService {
	Mono<UserResponse> createUser(Mono<CreateUserRequest> request);
	Mono<UserResponse> getUser(UUID userId);
	Flux<UserResponse> getAllUsers(int page,int limit);
}
