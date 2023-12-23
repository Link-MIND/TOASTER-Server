package com.app.linkmind.exception.model;

import com.app.linkmind.exception.Error;

public class NotFoundException extends CustomException {
	public NotFoundException(Error error, String message) {
		super(error, message);
	}
}
