package com.app.toaster.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.CustomException;

import jakarta.validation.ConstraintDefinitionException;
import lombok.NoArgsConstructor;

@RestControllerAdvice
@Component
@NoArgsConstructor
public class ControllerExceptionAdvice {
	/**
	 * custom error
	 */
	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ApiResponse> handleCustomException(CustomException e) {
		return ResponseEntity.status(e.getHttpStatus())
			.body(ApiResponse.error(e.getError(), e.getMessage()));
	}

	// @ExceptionHandler(IllegalArgumentException.class)
	// public ResponseEntity<Void> handleIllegalArgumentException(final IllegalArgumentException e) {
	// 	return ResponseEntity.badRequest().build();
	// }
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ApiResponse> handleConstraintDefinitionException(final MethodArgumentNotValidException e) {
		return ResponseEntity.status(e.getStatusCode())
			.body(ApiResponse.error(Error.BAD_REQUEST_VALIDATION, e.getMessage()));
	}
}
