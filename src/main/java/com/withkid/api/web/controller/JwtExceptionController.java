package com.withkid.api.web.controller;

import com.withkid.api.web.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

@ControllerAdvice
@RestController
public class JwtExceptionController {
	
	@ExceptionHandler(MalformedJwtException.class)
	public ResponseEntity<ErrorResponse> malformjwtExceptionHandler(){
		ErrorResponse body = ErrorResponse.builder().name(MalformedJwtException.class.getSimpleName())
				.msg("토큰의 전송되지 않았거나 일부만 전송되었습니다.").status(HttpStatus.UNAUTHORIZED).build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
	}
	
	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<ErrorResponse> signatureExceptionHandler(){
		ErrorResponse body = ErrorResponse.builder().name(SignatureException.class.getSimpleName())
				.msg("토큰이 위조되었습니다.").status(HttpStatus.UNAUTHORIZED).build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
	}
	
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorResponse> tokenExpiredHanler() {
		ErrorResponse body = ErrorResponse.builder().name(ExpiredJwtException.class.getSimpleName())
				.msg("토큰의 유효기간이 초과했습니다.").status(HttpStatus.UNAUTHORIZED).build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
	}
}
