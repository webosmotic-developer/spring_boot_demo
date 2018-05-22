package com.wo.demo.auth.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "user")
public class User {
	private Long id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String confirmPassword;
	private Set<Role> roles;
	private String token;
	private String provider;
	private String apiKey;
	private boolean active;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Transient
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Transient
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@ManyToMany
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Transient
	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	@Transient
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setActive(boolean isActive) {
		this.active = isActive;
	}
	

	public boolean isActive() {
		return active;
	}
}
