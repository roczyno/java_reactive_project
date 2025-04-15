package com.roczyno.reactive_project.service;

import com.roczyno.reactive_project.UserRepository;
import com.roczyno.reactive_project.dto.AlbumRes;
import com.roczyno.reactive_project.dto.CreateUserRequest;
import com.roczyno.reactive_project.dto.UserResponse;
import com.roczyno.reactive_project.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.UUID;


@Service
@Slf4j
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final Sinks.Many<UserResponse> usersSink;
	private final WebClient webClient;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, Sinks.Many<UserResponse> usersSink, WebClient webClient) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.usersSink = usersSink;
		this.webClient = webClient;
	}

	@Override
	public Mono<UserResponse> createUser(Mono<CreateUserRequest> request) {
		return request
				.flatMap(item->convertTOEntity(item))
				.flatMap(item->userRepository.save(item))
				.mapNotNull(item->convertToUserResponse(item))
				.doOnSuccess(savedUser->usersSink.tryEmitNext(savedUser));

	}

	@Override
	public Mono<UserResponse> getUser(UUID userId, String include, String jwt) {
		return userRepository.findById(userId)
				.map(item->convertToUserResponse(item))
				.flatMap(user->{
					if(include!=null && include.equals("albums")){
						return includeUserAlbums(user,jwt);
					}
					return Mono.just(user);
				});
	}

	private Mono<UserResponse> includeUserAlbums(UserResponse user, String jwt) {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.port(8084)
						.path("/albums")
						.queryParam("userId",user.id())
						.build())
				.header("Authorization",jwt)
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError,response->{
					return Mono.error(new RuntimeException("albums not found"));
				})
				.onStatus(HttpStatusCode::is5xxServerError,response->{
					return Mono.error(new RuntimeException("server error"));
				})
				.bodyToFlux(AlbumRes.class)
				.collectList()
				.map(albums-> user)
				.onErrorResume(e-> {
					log.error("error fetching...");
					return Mono.just(user);
				});

	}

	@Override
	public Flux<UserResponse> getAllUsers(int page, int limit) {
		Pageable pageable= PageRequest.of(page, limit);
		return userRepository.findAllBy(pageable)
				.map(user -> convertToUserResponse(user));
	}

	@Override
	public Flux<UserResponse> streamUser() {
		return usersSink.asFlux()
				.publish()
				.autoConnect(1);
	}

	private Mono<User> convertTOEntity(CreateUserRequest createUserRequest){
		return Mono.fromCallable(()->{
			User user= new User();
			BeanUtils.copyProperties(createUserRequest,user);
			user.setPassword(passwordEncoder.encode(createUserRequest.password()));
			return user;
		}).subscribeOn(Schedulers.boundedElastic());

	}

	private UserResponse convertToUserResponse(User user){
//		return new UserResponse(
//				user.getId(),
//				user.getFirstName(),
//				user.getLastName(),
//				user.getEmail()
//		);
		return null;
	}


	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return userRepository.findByEmail(username).map(user->
				org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
						.password(user.getPassword())
						.authorities(new ArrayList<>()).build());
	}
}
