package com.blogwaves.main.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
		info = @Info(
				title = "Blogging Application APIs", 
				description = "This is a backend application of Blogging Application", 
				version = "1.0", 
				contact = @Contact(
						name = "Sanskrati Varshney", 
						url = "https://sanskrati27.github.io/Portfolio/", 
						email = "varshneysanskrati27@gmail.com"
						)
				), 
				security = @SecurityRequirement(name = "bearerAuth") // Apply security globally
)
@SecurityScheme(
		name = "bearerAuth", 
		scheme = "bearer", 
		type = SecuritySchemeType.HTTP, bearerFormat = "JWT" // Specifies JWT token
																												
)
public class SwaggerConfiguration {

	@Bean
	GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder().group("blog-api").pathsToMatch("/api/**") // Adjust as per your API endpoints
				.build();
	}
}
