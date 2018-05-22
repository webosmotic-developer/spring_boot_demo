package com.wo.demo.jwt.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class JwtUserDetails implements UserDetails {

	private String userName;
	private long id;
	private String token;
	private Collection<? extends GrantedAuthority> grantedAuthorities;

	public JwtUserDetails(String userName, long id, String token, List<GrantedAuthority> grantedAuthorities) {
		this.userName = userName;
		this.id = id;
		this.token = token;
		this.grantedAuthorities = grantedAuthorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public long getId() {
		return id;
	}

	public String getToken() {
		return token;
	}
}