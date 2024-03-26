package com.app.toaster.controller.response.auth;

public record TokenHealthDto(boolean tokenHealth) {
	public static TokenHealthDto of(boolean tokenHealth){return new TokenHealthDto(tokenHealth);}
}
