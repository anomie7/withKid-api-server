package com.withkid.api.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.withkid.api.exception.EventNotFountException;

@ControllerAdvice
@RestController
public class EventExceptionController {

	@ExceptionHandler(EventNotFountException.class)
	public ResponseEntity<ErrorResponse> eventNotFoundHandler() {
		ErrorResponse body = ErrorResponse.builder().status(HttpStatus.NOT_FOUND).name(EventNotFountException.class.getSimpleName())
				.msg("이벤트를 찾을 수 없습니다.").build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}
}
