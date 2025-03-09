package com.blogwaves.main.config;

public class ConstantValues {

	// Using in Main file
	public static final Integer ROLE_ADMIN = 101;
	public static final Integer ROLE_USER = 102;

	public static final String ADMIN = "ROLE_ADMIN";
	public static final String USER = "ROLE_USER";

	// Using in SecurityConfig.java
	public static final String[] PUBLIC_URLS = { 
			"/api/auth/**",
			"/api/posts/",
			"/v3/api-docs/**",
			"/swagger-ui/**",
			"/swagger-ui.html" 
			};

	// Using in PostController
	public static final String PAGE_NUMBER = "0";
	public static final String PAGE_SIZE = "3";
	public static final String SORT_BY = "postId";
	public static final String SORT_DIR = "asc";
	
	//Using in JWTTokenHelper 
	//This value in milliseconds
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
}
