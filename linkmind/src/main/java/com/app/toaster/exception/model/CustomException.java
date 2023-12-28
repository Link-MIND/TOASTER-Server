package com.app.toaster.exception.model;

import com.app.toaster.exception.Error;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
	private final Error error;

	public CustomException(Error error, String message) {
		super(message);
		this.error = error;
	}

	public int getHttpStatus() {
		return error.getErrorCode();
	}
}
