package com.cesar.BookApi.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalAdvice{

	
	@ExceptionHandler({NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody()
	public ResponseEntity<?> handleNotFound() {
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Path not found");
	}

	
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<?> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex){
		
		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Media type not supported. Only supported JSON values.");
	}
	
	
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleMessageNotReadable(HttpMessageNotReadableException ex){
		
		return ResponseEntity.badRequest().body("Not readable value in body.");
	}
	

	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		
		String parameter = ex.getName();
		Object value = ex.getValue();
		String required = ex.getRequiredType().getSimpleName();
		
		String message = String.format("'%s' most being a type '%s' value for '%s'", value, required, parameter);
		
		return ResponseEntity.badRequest().body( message );	
	}
	
	
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingRequestParameter(MissingServletRequestParameterException ex) {
		
		String parameter = ex.getParameterName();
		
		String message = String.format("Parameter '%s' is required.", parameter);
		
		return ResponseEntity.badRequest().body( message );	
	}
	
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		
		return ResponseEntity.badRequest().body( ex.getFieldError().getDefaultMessage() );	
	}
	
	
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {

		StringBuilder message = new StringBuilder(); 
		
		ex.getConstraintViolations().stream().forEach( v -> message.append( v.getMessage() + " " ));
		
		return ResponseEntity.badRequest().body( message );	
	}
	
}
