package com.app.toaster.controller.response.auth;

public record TokenResponseDto(String accessToken, String refreshToken) {
	public static TokenResponseDto of(String accessToken, String refreshToken){
		return new TokenResponseDto(accessToken,refreshToken);
	}

}
