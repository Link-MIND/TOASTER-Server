package com.app.toaster.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.app.toaster.common.dto.ApiResponse;
// import com.app.toaster.config.UserId;
import com.app.toaster.controller.request.toast.IsReadDto;
import com.app.toaster.controller.request.toast.SaveToastDto;
import com.app.toaster.controller.response.toast.IsReadResponse;
import com.app.toaster.exception.Success;
import com.app.toaster.service.toast.ToastService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/toast")
@Validated
public class ToastController {
	private final ToastService toastService;

	@PostMapping("/save")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse createToast(
		@RequestHeader("userId") Long userId,
		@RequestBody @Valid SaveToastDto requestDto
	) {
		toastService.createToast(userId, requestDto);
		return ApiResponse.success(Success.CREATE_TOAST_SUCCESS);
	}

	@PatchMapping("/is-read")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<IsReadResponse> updateIsRead(
		@RequestHeader("userId") Long userId,
		@RequestBody IsReadDto requestDto
	){
		return ApiResponse.success(Success.UPDATE_ISREAD_SUCCESS, toastService.readToast(userId,requestDto));
	}

	@DeleteMapping("/delete")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse deleteToast(		//나중에 softDelete로 변경
		@RequestHeader("userId") Long userId,
		@RequestParam Long toastId
	) {
		toastService.deleteToast(userId, toastId);
		return ApiResponse.success(Success.DELETE_TOAST_SUCCESS);
	}

}
