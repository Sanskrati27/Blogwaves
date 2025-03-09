package com.blogwaves.main.payloads;

import lombok.Data;

@Data
public class JWTAuthRequest {

	private String username;	//Email
	
	private String password;
}
