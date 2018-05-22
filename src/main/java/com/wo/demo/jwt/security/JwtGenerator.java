package com.wo.demo.jwt.security;

import org.springframework.stereotype.Component;

import com.wo.demo.jwt.model.JwtUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtGenerator {

	public String generate(JwtUser jwtUser) {
		Claims claims = Jwts.claims().setSubject(jwtUser.getUserName());
		claims.put("userId", String.valueOf(jwtUser.getId()));
		claims.put("role", jwtUser.getRole());
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, "secret").compact();
	}
}
