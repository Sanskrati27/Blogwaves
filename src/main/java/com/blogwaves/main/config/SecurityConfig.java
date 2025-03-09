package com.blogwaves.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.blogwaves.main.security.CustomAccessDeniedHandler;
import com.blogwaves.main.security.JWTAuthenticationEntryPoint;
import com.blogwaves.main.security.JWTAuthenticationFilter;
import com.blogwaves.main.services.impl.CustomUserDetailService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableWebMvc
public class SecurityConfig {
	
	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Autowired
	private JWTAuthenticationEntryPoint jwtEntryPoint;
	
	@Autowired
	private CustomAccessDeniedHandler customAccessDeniedHandler;
	
	@Autowired
	private JWTAuthenticationFilter jwtFilter;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
        .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
        .authorizeHttpRequests(auth -> auth
        	.requestMatchers(ConstantValues.PUBLIC_URLS).permitAll()
            .anyRequest().authenticated() // Protect all other endpoints
//            .anyRequest().permitAll() // 🔥 Allow all requests
        )
        .sessionManagement(sess -> sess
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
        )
        .exceptionHandling(ex -> ex
        		.accessDeniedHandler(customAccessDeniedHandler) // Handles access denied (403)
        		.authenticationEntryPoint(jwtEntryPoint) // Handles authentication failures (401)
                
                ) 
        .authenticationProvider(daoAuthenticationProvider()) // Custom authentication provider
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);// Add JWT filter
        

		return http.build();
	}
	
	@Bean
	DaoAuthenticationProvider daoAuthenticationProvider() {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customUserDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
}
