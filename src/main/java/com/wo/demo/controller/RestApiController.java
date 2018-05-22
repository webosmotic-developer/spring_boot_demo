package com.wo.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wo.demo.auth.model.User;
import com.wo.demo.auth.service.UserService;
import com.wo.demo.mail.MailClient;
import com.wo.demo.uploadfile.storage.StorageService;

@RestController
@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RequestMapping("/rest")
public class RestApiController {

	@Autowired
	StorageService storageService;
	
	@Autowired
	private UserService userService;

	@Autowired
	MailClient mailClient; 
	
	List<String> files = new ArrayList<String>();
	
	@GetMapping(value = "/getUsers")
	public ResponseEntity<List<User>> getAllUsers() {
		mailClient.prepareAndSend("wo.dev01@gmail.com", "Test to send email form spring boot...");
		return new ResponseEntity<List<User>>(userService.findAll(), HttpStatus.OK);
	}
	
	@PostMapping(value = "/updateUser", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		userService.updateUser(user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PostMapping(value = "/uploadFile")
	public ResponseEntity<String> uploadFile(@RequestParam ("file") MultipartFile file, Model model) {
			try {
				System.out.println("in upload file ....."+file.getName());
				storageService.store(file);
				System.out.println("File uploaded....");
				model.addAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
				files.add(file.getOriginalFilename());
			} catch (Exception e) {
				model.addAttribute("message", "FAIL to upload " + file.getOriginalFilename() + "!");
			}
		return new ResponseEntity<String>("File uploaded successfully...", HttpStatus.OK);
	}
}
