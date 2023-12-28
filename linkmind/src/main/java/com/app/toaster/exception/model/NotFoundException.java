package com.app.toaster.exception.model;

import com.app.toaster.exception.Error;

public class NotFoundException extends CustomException {
	public NotFoundException(Error error, String message) {
		super(error, message);
	}
}
