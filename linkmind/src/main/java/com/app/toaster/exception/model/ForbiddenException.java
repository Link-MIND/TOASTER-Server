package com.app.toaster.exception.model;

import com.app.toaster.exception.Error;

public class ForbiddenException extends CustomException{
	public ForbiddenException(Error error, String message) {
		super(error, message);
	}

}