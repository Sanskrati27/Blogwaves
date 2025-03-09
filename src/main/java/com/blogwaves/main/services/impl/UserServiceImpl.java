package com.blogwaves.main.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogwaves.main.config.ConstantValues;
import com.blogwaves.main.entities.Role;
import com.blogwaves.main.entities.User;
import com.blogwaves.main.exceptions.ResourceNotFoundException;
import com.blogwaves.main.payloads.UserDto;
import com.blogwaves.main.repositories.RoleRepository;
import com.blogwaves.main.repositories.UserRepository;
import com.blogwaves.main.services.UserService;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public UserDto createUser(UserDto userDto) {

		User user = dtoToEntity(userDto);

		// Encoded the password
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Roles
		Role role = roleRepository.findById(ConstantValues.ROLE_ADMIN).get();
		user.getRoles().add(role);

		User createdUser = userRepository.save(user);
		return entityToDto(createdUser);
	}

	@Override
	public UserDto registerUser(UserDto userDto) {
		User user = dtoToEntity(userDto);

		// Encoded the password
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Roles
		Role role = roleRepository.findById(ConstantValues.ROLE_USER).get();
		user.getRoles().add(role);

		User createdUser = userRepository.save(user);
		return entityToDto(createdUser);
	}

	@Override
	public UserDto getUserById(int userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

		return entityToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {

		List<User> users = userRepository.findAll();

		List<UserDto> userDtos = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

		return userDtos;
	}

	@Override
	public UserDto updateUser(UserDto userDto, int userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());

		User updatedUser = userRepository.save(user);

		// UserDto u = new UserDto();
		return entityToDto(updatedUser);
		// return updatedUser;
	}

	@Override
	public void deleteUserById(int userId) {
		userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
		// userRepository.delete(user);

		userRepository.deleteById(userId);
	}

	private User dtoToEntity(UserDto userDto) {

//		User user = new User();
//
//		user.setId(userDto.getId());
//		user.setName(userDto.getName());
//		user.setEmail(userDto.getEmail());
//		user.setPassword(userDto.getPassword());
//		user.setAbout(userDto.getAbout());

		User user = modelMapper.map(userDto, User.class);

		return user;
	}

	private UserDto entityToDto(User user) {

//		UserDto userDto = new UserDto();
//
//		userDto.setId(user.getId());
//		userDto.setName(user.getName());
//		userDto.setEmail(user.getEmail());
//		userDto.setPassword(user.getPassword());
//		userDto.setAbout(user.getAbout());

		UserDto userDto = modelMapper.map(user, UserDto.class);

		return userDto;
	}

}
