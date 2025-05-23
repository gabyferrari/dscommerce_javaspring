package com.devsuperior.dscommerce.controllers.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscommerce.dto.CustomError;
import com.devsuperior.dscommerce.dto.ValidationError;
import com.devsuperior.dscommerce.services.exceptions.DataBaseException;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
	
	 @ExceptionHandler(ResourceNotFoundException.class)
	 public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		 HttpStatus status = HttpStatus.NOT_FOUND; //erro 404
		 CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
		 return ResponseEntity.status(status).body(err);
	 }
	 
	 @ExceptionHandler(DataBaseException.class)
	 public ResponseEntity<CustomError> dataBase(DataBaseException e, HttpServletRequest request) {
		 HttpStatus status = HttpStatus.BAD_REQUEST; //erro 400
		 CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
		 return ResponseEntity.status(status).body(err);
	 }

	 @ExceptionHandler(MethodArgumentNotValidException.class)
	 public ResponseEntity<CustomError> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
		 HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY; //erro 422
		 ValidationError err = new ValidationError(Instant.now(), status.value(), "Dados inválidos", request.getRequestURI());
		
		 for (FieldError f : e.getBindingResult().getFieldErrors()) { //vai percorrer todos os erros que estiverem na lista FieldErrors dando o apelido de f
			 err.addError(f.getField(), f.getDefaultMessage());
		 }
		 return ResponseEntity.status(status).body(err);
	 }
	 
	 @ExceptionHandler(ForbiddenException.class)
	 public ResponseEntity<CustomError> forbidden(ForbiddenException e, HttpServletRequest request) {
		 HttpStatus status = HttpStatus.FORBIDDEN; //erro 403, nao permite que um cliente sem ser admin acesse pedidos de outros clientes a nao ser o dela mesmo
		 CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
		 return ResponseEntity.status(status).body(err);
	 }
}
