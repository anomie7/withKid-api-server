package com.withkid.api.log.service;

import org.springframework.stereotype.Service;

import com.withkid.api.exception.JwtTypeNotMatchedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {
	final private String SECRET_KEY = "depromeet_mini_prj";
	

	public Jws<Claims> getBody(String jwt) {
			Jws<Claims> re = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt);
			return re;
	}
	
	public Jws<Claims> thisAccessTokenUsable(String accessToken) throws ExpiredJwtException {
		Jws<Claims> re = getBody(accessToken);
		if (!re.getHeader().get("type").equals("access-token")) {
			throw new JwtTypeNotMatchedException();
		}
		return re;
	}
}
