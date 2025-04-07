package com.roczyno.reactive_project.dto;

public record AuthenticationRequest(
		String email,
		String password
) {
}
