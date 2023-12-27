package com.app.linkmind.exception.model;

import com.app.linkmind.exception.Error;

public class UnauthorizedException extends CustomException{
	public UnauthorizedException(Error error, String message) {
		super(error, message);
	}

}
