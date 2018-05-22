package com.wo.demo.jwt.security;

import org.springframework.stereotype.Component;

import com.wo.demo.jwt.model.JwtUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtValidator {

	String secretKey = "secret";

	public JwtUser validate(String token) {

		JwtUser jwtUser = null;
		try {
			Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
			jwtUser = new JwtUser();
			jwtUser.setUserName(body.getSubject());
			jwtUser.setId(Long.parseLong((String) body.get("userId")));
			jwtUser.setRole((String) body.get("role"));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return jwtUser;
	}
}
