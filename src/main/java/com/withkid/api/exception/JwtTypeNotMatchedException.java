package com.withkid.api.exception;

import io.jsonwebtoken.JwtException;

public class JwtTypeNotMatchedException extends JwtException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JwtTypeNotMatchedException() {
		super("토큰의 타입이 다릅니다.");
	}

}
