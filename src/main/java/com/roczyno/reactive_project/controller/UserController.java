package com.roczyno.reactive_project.controller;

import com.roczyno.reactive_project.dto.CreateUserRequest;
import com.roczyno.reactive_project.dto.UserResponse;
import com.roczyno.reactive_project.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public Mono<ResponseEntity<UserResponse>> createUser(@RequestBody Mono<CreateUserRequest> createUserRequest) {
		return userService.createUser(createUserRequest)
				.map(userResponse->ResponseEntity
						.status(HttpStatus.CREATED)
						.body(userResponse));
	}

	@GetMapping("/{userId}")
	@PreAuthorize("authentication.principal.equals(#userId.toString())")
	public Mono<ResponseEntity<UserResponse>> getUser(@PathVariable UUID userId,@RequestParam(name = "include",
			required = false) String include,
													  @RequestHeader(name = "Authorization") String jwt) {
		return userService.getUser(userId,include,jwt)
				.map(userResponse -> ResponseEntity.status(HttpStatus.OK)
						.location(URI.create("/users/" + userId))
						.body(userResponse))
				.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));

	}

	@GetMapping
	public Flux<ResponseEntity<UserResponse>> getUsers(
			@RequestParam(value = "offset", defaultValue = "0") int page,
			@RequestParam(value = "limit",defaultValue = "50") int limit
			) {
		return userService.getAllUsers(page,limit)
				.map(userResponse -> ResponseEntity.status(HttpStatus.OK).body(userResponse));
	}

	@GetMapping(value = "/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<UserResponse> streamUsers(){
		return userService.streamUser();
	}
}
