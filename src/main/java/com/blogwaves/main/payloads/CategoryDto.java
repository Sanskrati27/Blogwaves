package com.blogwaves.main.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

	private int categoryId;

	@NotBlank
	@Size(min = 10, max = 50, message = "Title must contain 10 to 50 characters.")
	private String title;

	private String description;
}
