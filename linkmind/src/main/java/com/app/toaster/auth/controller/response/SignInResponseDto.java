package com.app.toaster.auth.controller.response;

public record SignInResponseDto(Long userId, String accessToken, String refreshToken, String fcmToken,
                                Boolean isRegistered, Boolean fcmIsAllowed, String profile, String os) {
    public static SignInResponseDto of(Long userId, String accessToken, String refreshToken, String fcmToken,
                                       Boolean isRegistered, Boolean fcmIsAllowed, String profile, String os) {
        return new SignInResponseDto(userId, accessToken, refreshToken, fcmToken, isRegistered, fcmIsAllowed, profile, os);
    }
}
