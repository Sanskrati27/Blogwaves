package com.blogwaves.main.services;

import java.util.List;

import com.blogwaves.main.payloads.UserDto;

public interface UserService {

	// Create new User
	public UserDto createUser(UserDto userDto);

	// Registering new User
	public UserDto registerUser(UserDto userDto);

	// Get Single User
	public UserDto getUserById(int userId);

	// Get all users
	public List<UserDto> getAllUsers();

	// Update a user
	public UserDto updateUser(UserDto userDto, int userId);

	// Delete a user
	public void deleteUserById(int userId);
}
