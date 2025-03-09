package com.blogwaves.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogwaves.main.exceptions.InvalidDetails;
import com.blogwaves.main.payloads.JWTAuthRequest;
import com.blogwaves.main.payloads.JWTAuthResponse;
import com.blogwaves.main.payloads.UserDto;
import com.blogwaves.main.security.JWTTokenHelper;
import com.blogwaves.main.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private JWTTokenHelper jwtTokenHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<JWTAuthResponse> createToken(
			@RequestBody JWTAuthRequest request) throws Exception{
		
		authenticate(request.getUsername(), request.getPassword());
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		String token = jwtTokenHelper.generateToken(userDetails);
		
		JWTAuthResponse response = new JWTAuthResponse();
		response.setToken(token);
		
		return new ResponseEntity<JWTAuthResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {

		UserDto registeredUserDto = userService.registerUser(userDto);

		return new ResponseEntity<>(registeredUserDto, HttpStatus.CREATED);
	}

	private void authenticate(String username, String password) throws Exception {
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		
		try {
			authManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			System.out.println("Invalid details");
			throw new InvalidDetails("Invalid password");
		}
		
	}
}
