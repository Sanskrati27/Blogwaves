package com.blogwaves.main.exceptions;

public class InvalidDetails extends RuntimeException{

	public InvalidDetails() {
		super();	
	}

	public InvalidDetails(String message) {
		super(message);
		
	}
	
}
