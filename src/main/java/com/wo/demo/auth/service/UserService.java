package com.wo.demo.auth.service;

import java.util.List;

import com.wo.demo.auth.model.User;

public interface UserService {
	void save(User user);

	User findByUsername(String username);

	List<User> findAll();

	void updateUser(User user);
}
