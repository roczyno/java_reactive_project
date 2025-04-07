package com.roczyno.reactive_project.exception;


import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateKeyException.class)
	public Mono<ErrorResponse> handleDuplicateException(DuplicateKeyException duplicateKeyException){
		return Mono.just( ErrorResponse.builder(duplicateKeyException, HttpStatus.CONFLICT,duplicateKeyException.getMessage())
				.build());
	}

	@ExceptionHandler(Exception.class)
	public Mono<ErrorResponse> handleAllException(Exception exception){
		return Mono.just( ErrorResponse.builder(exception, HttpStatus.CONFLICT,exception.getMessage())
				.build());
	}
	@ExceptionHandler(BadCredentialsException.class)
	public Mono<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex){
		return Mono.just(ErrorResponse.builder(ex,HttpStatus.UNAUTHORIZED,ex.getMessage()).build());
	}
}
