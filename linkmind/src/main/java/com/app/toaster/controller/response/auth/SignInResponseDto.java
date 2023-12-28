package com.app.toaster.controller.response.auth;

public record SignInResponseDto(Long userId, String accessToken, String refreshToken, String fcmToken, Boolean isRegistered,Boolean FcmIsAllowed) {
	public static SignInResponseDto of(Long userId, String accessToken, String refreshToken, String fcmToken,
		Boolean isRegistered, Boolean fcmIsAllowed){
		return new SignInResponseDto(userId,accessToken, refreshToken,fcmToken,isRegistered,fcmIsAllowed);
	}
}
