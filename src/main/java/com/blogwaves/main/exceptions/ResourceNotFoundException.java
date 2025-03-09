package com.blogwaves.main.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{
	
	String entityName;
	String fieldName;
	int resourceField;
	
	public ResourceNotFoundException(String entityName, String fieldName, int resourceField) {
		super(String.format("%s not found with %s : %d", entityName, fieldName, resourceField));
		this.entityName = entityName;
		this.fieldName = fieldName;
		this.resourceField = resourceField;
	}

	public ResourceNotFoundException(String entityName, String fieldName) {
		super(String.format("%s not found with %s ", entityName, fieldName));
		this.entityName = entityName;
		this.fieldName = fieldName;
	}
	
}
