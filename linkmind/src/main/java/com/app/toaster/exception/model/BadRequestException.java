package com.app.toaster.exception.model;

import com.app.toaster.exception.Error;

public class BadRequestException extends CustomException{
	public BadRequestException(Error error, String message) {
		super(error, message);
	}

}
