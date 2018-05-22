package com.wo.demo.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.wo.demo.auth.model.User;
import com.wo.demo.controller.TokenController;
import com.wo.demo.jwt.model.JwtUser;

@Service
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	TokenController tokenController;

	@Override
	public String findLoggedInUsername() {
		Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
		if (userDetails instanceof UserDetails) {
			return ((UserDetails) userDetails).getUsername();
		}
		return null;
	}

	@Override
	public String generateJwtToken(User user) {
		return tokenController.generateToken(new JwtUser(user.getId(), user.getUsername(), "ADMIN"));
	}
}
