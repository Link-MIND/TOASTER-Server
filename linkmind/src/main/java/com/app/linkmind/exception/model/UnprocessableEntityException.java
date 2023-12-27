package com.app.linkmind.exception.model;

import com.app.linkmind.exception.Error;

public class UnprocessableEntityException extends CustomException{
	public UnprocessableEntityException(Error error, String message) {
		super(error, message);
	}
}
