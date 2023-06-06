package com.oshop.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.oshop.enumeration.Status;
import com.oshop.model.response.GenericResponse;
import com.oshop.model.response.ResponseError;
import com.oshop.utils.AppConstants;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({ NotFoundException.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ResponseEntity<GenericResponse<String>> notFound(NotFoundException exception) {
		GenericResponse<String> genericResponse = new GenericResponse<String>();
		genericResponse.header(Status.FAILED, HttpStatus.NOT_FOUND, exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
	}
	
	@ExceptionHandler({ ConstraintViolationException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<GenericResponse<String>> constraintVoilation(ConstraintViolationException exception) {
		List<ResponseError> errors = new ArrayList<>();
		
		exception.getConstraintViolations().stream().forEach(constraint -> {
			errors.add(new ResponseError(constraint.getPropertyPath().toString(), constraint.getMessage()));			
		});
		GenericResponse<String> genericResponse = new GenericResponse<String>();
		genericResponse.header(Status.FAILED, HttpStatus.BAD_REQUEST, AppConstants.FIELD_VALIDATIONS_MESSAGE);
		genericResponse.setErrors(errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);	
	}
	
	@ExceptionHandler({ HttpMessageNotReadableException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<GenericResponse<String>> httpMessageNotReadable(HttpMessageNotReadableException exception) {
		GenericResponse<String> genericResponse = new GenericResponse<String>();
		genericResponse.header(Status.FAILED, HttpStatus.BAD_REQUEST, exception.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);	
	}
	
}
