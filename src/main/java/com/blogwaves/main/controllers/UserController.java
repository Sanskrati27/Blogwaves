package com.blogwaves.main.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogwaves.main.payloads.ApiResponse;
import com.blogwaves.main.payloads.UserDto;
import com.blogwaves.main.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	//Only admin can create new user
	// POST - create new user
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {

		UserDto createUserDto = userService.createUser(userDto);

		return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
	}

	//Only admin can get all user details
	// GET - get all users
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers() {

		return ResponseEntity.ok(userService.getAllUsers());
	}
	
	//Only admin can get the user details by id
	// GET - get user by id
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable int userId) {

		return ResponseEntity.ok(userService.getUserById(userId));
	}

	// PUT - update a user

	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable int userId) {
		UserDto updatedUser = userService.updateUser(userDto, userId);

		return ResponseEntity.ok(updatedUser);
	}

	//Only admin can delete
	// DELETE - delete a user
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable int userId) {

		userService.deleteUserById(userId);

		return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted successfully", true), HttpStatus.OK);
	}

}
