package com.app.toaster.common.advice;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.CustomException;
import com.app.toaster.external.client.slack.SlackApi;

import io.sentry.Sentry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintDefinitionException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Component
@RequiredArgsConstructor
public class ControllerExceptionAdvice {
	private final SlackApi slackApi;

	/**
	 * custom error
	 */
	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ApiResponse> handleCustomException(CustomException e) {
		Sentry.captureException(e);
		return ResponseEntity.status(e.getHttpStatus())
				.body(ApiResponse.error(e.getError(), e.getMessage()));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ApiResponse> handleConstraintDefinitionException(final MethodArgumentNotValidException e) {
		FieldError fieldError = e.getBindingResult().getFieldError();
		Sentry.captureException(e);
		return ResponseEntity.status(e.getStatusCode())
				.body(ApiResponse.error(Error.BAD_REQUEST_VALIDATION, fieldError.getDefaultMessage()));
	}

	@ExceptionHandler(MalformedURLException.class)
	protected ApiResponse handleConstraintDefinitionException(final MalformedURLException e) {
		Sentry.captureException(e);
		return ApiResponse.error(Error.MALFORMED_URL_EXEPTION, Error.MALFORMED_URL_EXEPTION.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DateTimeParseException.class)
	protected ApiResponse handleDateTimeParseException(final DateTimeParseException e) {
		return ApiResponse.error(Error.BAD_REQUEST_REMIND_TIME, Error.BAD_REQUEST_REMIND_TIME.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ApiResponse handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
		return ApiResponse.error(Error.REQUEST_METHOD_VALIDATION_EXCEPTION, e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	protected ApiResponse handleHttpMediaTypeNotSupportedException(final HttpMediaTypeNotSupportedException e) {
		return ApiResponse.error(Error.REQUEST_MEDIA_TYPE_VALIDATION_EXCEPTION, e.getMessage());
	}

	
   /**
	 * 500 Internal Server Error
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	protected ApiResponse<Object> handleException(final Exception error, final HttpServletRequest request) throws
		IOException {
		slackApi.sendAlert(error, request);
		Sentry.captureException(error);
		return ApiResponse.error(Error.INTERNAL_SERVER_ERROR);
	}

}
