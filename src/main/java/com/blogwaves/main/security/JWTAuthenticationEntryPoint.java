package com.blogwaves.main.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		response.setContentType("application/json"); // Ensure JSON response
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Create a JSON response manually
        response.getWriter().write("{\"status\":401, \"error\":\"Unauthorized\", \"message\":\"Access Denied!!\"}");
        response.getWriter().flush();
		
	}

}
