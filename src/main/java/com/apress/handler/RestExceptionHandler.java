package com.apress.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.apress.controller.error.ErrorDetail;
import com.apress.controller.error.ValidationError;
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
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
		
		ErrorDetail errorDetail = new ErrorDetail();

		errorDetail.setTimeStamp(new Date().getTime());
		errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());

		String requestPath = (String) request.getAttribute("javax.servlet.error.request_uri");

		if (requestPath == null) {
			requestPath = request.getRequestURI();
		}

		errorDetail.setTitle("Validation failed");
		errorDetail.setDetail("Input validation failed");
		errorDetail.setDeveloperMessage(exception.getClass().getName());

		// creo las instancias de FieldError
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

		// itero los field errors
		for (FieldError fe : fieldErrors) {

			// obtengo una lista de ValidationError pasandole el campo como key que va a buscar al Map errors ==> ValidationError.errors
			List<ValidationError> validationErrorList = errorDetail.getErrors().get(fe.getField());
			
			// si no hay un ValidationError en el map de errorDetail.errors devuelve null
			if (validationErrorList == null) {
				validationErrorList = new ArrayList<ValidationError>();
				errorDetail.getErrors().put(fe.getField(), validationErrorList);
			}
			ValidationError validationError = new ValidationError();
			validationError.setCode(fe.getCode());
			validationError.setMessage(fe.getField() + " " + fe.getDefaultMessage());
			validationErrorList.add(validationError);
		}
		return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
	}
	
}
