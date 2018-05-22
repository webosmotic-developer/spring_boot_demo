package com.wo.demo.auth.service;

import com.wo.demo.auth.model.User;

public interface SecurityService {
	String findLoggedInUsername();

	String generateJwtToken(User user);
}
