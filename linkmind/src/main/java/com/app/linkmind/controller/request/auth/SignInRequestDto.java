package com.app.linkmind.controller.request.auth;

public record SignInRequestDto(String socialType, String fcmToken) {
}
