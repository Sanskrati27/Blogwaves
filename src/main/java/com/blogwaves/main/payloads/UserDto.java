package com.blogwaves.main.payloads;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

	private int id;
	
	@NotBlank
	@Size(min = 4, message = "Name must contain 4 characters")
	private String name;
	
	@NotBlank
	@Email(message = "Email address is not valid")
	private String email;
	
	@NotBlank
	@Size(min = 6, max = 12, message = "Password must contain 6 to 12 characters")
	private String password;
	
	@NotBlank
	private String about;
	
	private Set<RoleDto> roles = new HashSet<>();
}
