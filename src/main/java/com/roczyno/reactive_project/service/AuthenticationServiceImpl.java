package com.roczyno.reactive_project.service;

import com.roczyno.reactive_project.UserRepository;
import com.roczyno.reactive_project.entity.User;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
	private final ReactiveAuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final JwtService jwtService;

	public AuthenticationServiceImpl(ReactiveAuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.jwtService = jwtService;
	}

	@Override
	public Mono<Map<String, String>> authenticate(String email, String password) {
		return authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(email,password))
				.then(getUserDetails(email))
				.map(user-> createAuthResponse(user));

	}

	private Map<String, String> createAuthResponse(User user) {
		Map<String, String> result= new HashMap<>();
		result.put("userId",user.getId().toString());
		result.put("jwt", jwtService.generateJwt(user.getId().toString()));
		return result;
	}

	private Mono<User> getUserDetails(String username){
		return userRepository.findByEmail(username);
	}
}
