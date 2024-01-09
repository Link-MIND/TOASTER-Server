package com.app.toaster.exception.model.net;

import java.io.IOException;

import com.app.toaster.exception.Error;

import lombok.Getter;

@Getter
public class CustomJavaNetException extends IOException {
	private final Error error;

	public CustomJavaNetException(Error error, String message) {
		super(message);
		this.error = error;
	}

	public int getHttpStatus() {
		return error.getErrorCode();
	}

}
