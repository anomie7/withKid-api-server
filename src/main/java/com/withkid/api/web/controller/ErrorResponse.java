package com.withkid.api.web.controller;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
class ErrorResponse {
	private String name;
	private String msg;
	private HttpStatus status;
}
