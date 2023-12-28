package com.app.toaster.controller.request.auth;

public record SignInRequestDto(String socialType, String fcmToken) {
}
