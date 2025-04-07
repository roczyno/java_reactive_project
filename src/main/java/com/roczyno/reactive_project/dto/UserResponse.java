package com.roczyno.reactive_project.dto;

import lombok.Builder;

import java.util.UUID;


public record UserResponse(
		UUID id,
		String firstName,
		String lastName,
		String email
) {

}
