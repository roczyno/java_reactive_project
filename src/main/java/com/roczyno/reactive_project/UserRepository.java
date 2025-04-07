package com.roczyno.reactive_project;

import com.roczyno.reactive_project.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository extends ReactiveCrudRepository<User, UUID> {
	Flux<User> findAllBy(Pageable pageable);
	Mono<User> findByEmail(String username);
}
