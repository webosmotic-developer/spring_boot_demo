package com.wo.demo.jwt.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.wo.demo.jwt.model.JwtAuthenticationToken;
import com.wo.demo.jwt.model.JwtUser;
import com.wo.demo.jwt.model.JwtUserDetails;

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired
	JwtValidator jwtValidator;

	@Override
	public boolean supports(Class<?> aclass) {
		return JwtAuthenticationToken.class.isAssignableFrom(aclass);
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken token)
			throws AuthenticationException {
	}

	@Override
	protected UserDetails retrieveUser(String userName,
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) usernamePasswordAuthenticationToken;
		String token = jwtAuthenticationToken.getToken();
		JwtValidator validator = new JwtValidator();
		JwtUser jwtUser = validator.validate(token);
		if (jwtUser == null) {
			throw new RuntimeException("Token is Incorrect");
		}
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(jwtUser.getRole());
		return new JwtUserDetails(jwtUser.getUserName(), jwtUser.getId(), token, grantedAuthorities);
	}
}
