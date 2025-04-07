package com.roczyno.reactive_project.dto;

public record CreateUserRequest(
		String firstName,
		String lastName,
		String email,
		String password
) {
}
