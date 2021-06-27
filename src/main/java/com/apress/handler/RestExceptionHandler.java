package com.apress.handler;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.apress.controller.error.ErrorDetail;
import com.apress.exception.ResourceNotFoundException;

@ControllerAdvice
public class RestExceptionHandler {

	// cada vez que se invoque un ResourceNotFoundException se viene aca a ejecutar handlerResourceNotFoundException
	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<?> handlerResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request) {
		
		// se crea intancia ErrorDetail para pasarle al body del response
		ErrorDetail errorDetail = new ErrorDetail();
		
		errorDetail.setTimeStamp(new Date().getTime());
		errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
		errorDetail.setTitle("Resource not found");
		errorDetail.setDetail(exception.getMessage());
		errorDetail.setDeveloperMessage(exception.getClass().getName());

		return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
	}

}
