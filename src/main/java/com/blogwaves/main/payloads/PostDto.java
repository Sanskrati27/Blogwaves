package com.blogwaves.main.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

	private int postId;
	
	@NotBlank
	@Size(min = 10, max = 100, message = "Title must contain 10 to 50 characters.")
	private String title;

	@NotBlank
	@Size(min = 100, message = "Content must be of minimum 100 characters.")
	private String content;

	private String imageName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Date postDate;

	private CategoryDto category;

	private UserDto user;
	
	private Set<CommentDto> comments = new HashSet<>();
}
