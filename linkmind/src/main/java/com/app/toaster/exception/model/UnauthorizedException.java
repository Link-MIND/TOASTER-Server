package com.app.toaster.exception.model;

import com.app.toaster.exception.Error;

public class UnauthorizedException extends CustomException{
	public UnauthorizedException(Error error, String message) {
		super(error, message);
	}

}
