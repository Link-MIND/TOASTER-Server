package com.app.toaster.exception.model;

import com.app.toaster.exception.Error;

public class UnprocessableEntityException extends CustomException{
	public UnprocessableEntityException(Error error, String message) {
		super(error, message);
	}
}
