package com.blogwaves.main.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostNotFoundException extends RuntimeException {

	String entityName;
	String fieldName;
	String keyword;
	
	public PostNotFoundException(String entityName, String fieldName, String keyword) {
		super(String.format("No such %s containing ' %s ' in %s", entityName, keyword, fieldName));
		this.entityName = entityName;
		this.fieldName = fieldName;
		this.keyword = keyword;
	}
		
}
