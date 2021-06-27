package com.apress.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4784403740433424557L;

	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(final String message) {
		super(message);
	}

	public ResourceNotFoundException(final String message, Throwable cause) {
		super(message);
	}
	
}
