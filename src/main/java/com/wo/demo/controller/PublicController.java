package com.wo.demo.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wo.demo.auth.model.APIKey;
import com.wo.demo.auth.model.GenericResponse;
import com.wo.demo.auth.model.Role;
import com.wo.demo.auth.model.User;
import com.wo.demo.auth.service.SecurityService;
import com.wo.demo.auth.service.UserService;
import com.wo.demo.auth.validator.UserValidator;

@RestController
@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RequestMapping("/public")
@EnableConfigurationProperties
public class PublicController {

	@Value("${timeIntervalPerRequestInMilliSeconds}")
	Long timeIntervalPerRequestInMilliSeconds;

	@Value("${suspendedTimeIntervalInMilliSeconds}")
	Long suspendedTimeIntervalInMilliSeconds;

	@Value("${limit}")
	int limit;

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private ServletContext servletContext;

	@PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> registration(@RequestBody User userForm, BindingResult bindingResult) {
		if ("google".equals(userForm.getProvider())) {
			if (userService.findByUsername(userForm.getUsername()) == null) {
				userValidator.validate(userForm, bindingResult);
			}
		} else {
			userValidator.validate(userForm, bindingResult);
		}

		if (bindingResult.hasErrors()) {
			return new ResponseEntity<User>(HttpStatus.EXPECTATION_FAILED);
		}

		if (userService.findByUsername(userForm.getUsername()) == null) {
			Set<Role> roles = new HashSet<>();
			Role role = new Role();
			role.setName("USER");
			roles.add(role);
			userForm.setRoles(roles);
			userService.save(userForm);
		}

		User user = userService.findByUsername(userForm.getUsername());
		user.setToken(securityService.generateJwtToken(user));
		generateAPIKey(user);

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	private void generateAPIKey(User user) {
		try {
			InetAddress.getLocalHost();
			user.setApiKey(InetAddress.getLocalHost().getHostAddress().replace(".", ""));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> login(@RequestBody User userForm) {

		User user = userService.findByUsername(userForm.getUsername());
		if (!bCryptPasswordEncoder.matches(userForm.getPassword(), user.getPassword())) {
			return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
		}

		user.setToken(securityService.generateJwtToken(user));
		generateAPIKey(user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@PostMapping(value = "/google", produces = "application/json")
	@ResponseBody
	public ResponseEntity<User> googleRegistration(@RequestBody User user) {
		if (userService.findByUsername(user.getUsername()) == null) {
			Set<Role> roles = new HashSet<>();
			Role role = new Role();
			role.setName("USER");
			roles.add(role);
			user.setRoles(roles);
			userService.save(user);
		}

		User savedUser = userService.findByUsername(user.getUsername());
		savedUser.setToken(securityService.generateJwtToken(savedUser));
		return new ResponseEntity<User>(savedUser, HttpStatus.OK);
	}

	@RequestMapping("/rate_limit/{aPIKey}/{userName}")
	GenericResponse getHotelsByCityId(@PathVariable String aPIKey, @PathVariable String userName) {
		GenericResponse genericResponse = new GenericResponse(500, "FAILED", null);
		APIKey aPIKeyIns = null;
		if (this.getServletContext().getAttribute(aPIKey) != null) {
			aPIKeyIns = (APIKey) this.getServletContext().getAttribute(aPIKey);
			Long diffOfLastTime = System.currentTimeMillis() - aPIKeyIns.getLastHttpCallTimestamp();
			if (aPIKeyIns.getCount().get() != 0) {
				if (aPIKeyIns.getCount().get() <= limit) {
					if (diffOfLastTime <= timeIntervalPerRequestInMilliSeconds) {
						genericResponse.setStatus(500);
						genericResponse.setMsg("REQUEST REJECTED for next "
								+ timeIntervalPerRequestInMilliSeconds / 1000.0f + " seconds.");
					} else {
						getUser(userName, genericResponse, aPIKeyIns);
					}
				} else {
					aPIKeyIns.getCount().set(0);
					aPIKeyIns.setLastHttpCallTimestamp(System.currentTimeMillis());
					genericResponse.setStatus(500);
					genericResponse.setMsg("REQUEST REJECTED for next " + suspendedTimeIntervalInMilliSeconds / 60000.0f
							+ " seconds.");
				}
			} else {
				if (diffOfLastTime <= suspendedTimeIntervalInMilliSeconds) {
					genericResponse.setStatus(500);
					genericResponse.setMsg("REQUEST REJECTED for next " + suspendedTimeIntervalInMilliSeconds / 60000.0f
							+ " minutes.");
				} else {
					getUser(userName, genericResponse, aPIKeyIns);
				}
			}
		} else {
			this.getServletContext().setAttribute(aPIKey, new APIKey(new AtomicInteger(1), System.currentTimeMillis()));
			getUser(userName, genericResponse, aPIKeyIns);
		}
		return genericResponse;
	}

	private void getUser(String userName, GenericResponse genericResponse, APIKey aPIKeyIns) {
		User user = userService.findByUsername(userName);
		genericResponse.setStatus(200);
		genericResponse.setMsg("SUCCESS");
		genericResponse.setObj(user);
		if (aPIKeyIns != null) {
			aPIKeyIns.getCount().getAndIncrement();
			aPIKeyIns.setLastHttpCallTimestamp(System.currentTimeMillis());
		}
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public Long getTimeIntervalPerRequestInMilliSeconds() {
		return timeIntervalPerRequestInMilliSeconds;
	}

	public Long getSuspendedTimeIntervalInMilliSeconds() {
		return suspendedTimeIntervalInMilliSeconds;
	}

}
