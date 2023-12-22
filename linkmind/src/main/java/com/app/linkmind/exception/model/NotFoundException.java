package com.app.linkmind.exception.model;

public class NotFoundException extends CustomException {
	public NotFoundException(Error error, String message) {
		super(error, message);
	}
}
