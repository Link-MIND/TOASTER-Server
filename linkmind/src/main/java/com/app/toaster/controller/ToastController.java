package com.app.toaster.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.config.UserId;
import com.app.toaster.controller.request.toast.SaveToastDto;
import com.app.toaster.controller.response.auth.SignInResponseDto;
import com.app.toaster.exception.Success;
import com.app.toaster.service.toast.ToastService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/toast")
public class ToastController {
	private final ToastService toastService;

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse createToast(
		@UserId Long userId,
		SaveToastDto requestDto
	) {
		toastService.createToast(userId, requestDto);
		return ApiResponse.success(Success.CREATE_TOAST_SUCCESS, Success.CREATE_TOAST_SUCCESS.getMessage());
	}
}
