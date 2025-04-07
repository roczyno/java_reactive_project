package com.roczyno.reactive_project.service;

import com.roczyno.reactive_project.UserRepository;
import com.roczyno.reactive_project.dto.CreateUserRequest;
import com.roczyno.reactive_project.dto.UserResponse;
import com.roczyno.reactive_project.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.UUID;


@Service

public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Mono<UserResponse> createUser(Mono<CreateUserRequest> request) {
		return request
				.flatMap(item->convertTOEntity(item))
				.flatMap(item->userRepository.save(item))
				.mapNotNull(item->convertToUserResponse(item));

	}

	@Override
	public Mono<UserResponse> getUser(UUID userId) {
		return userRepository.findById(userId)
				.map(item->convertToUserResponse(item));
	}

	@Override
	public Flux<UserResponse> getAllUsers(int page, int limit) {
		Pageable pageable= PageRequest.of(page, limit);
		return userRepository.findAllBy(pageable)
				.map(user -> convertToUserResponse(user));
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
		return new UserResponse(
				user.getId(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail()
		);
	}


	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return userRepository.findByEmail(username).map(user->
				org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
						.password(user.getPassword())
						.authorities(new ArrayList<>()).build());
	}
}
