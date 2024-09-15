package com.app.toaster.popup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.toaster.popup.controller.request.PopUpRequestDto;
import com.app.toaster.popup.controller.response.InvisibleResponseDto;
import com.app.toaster.popup.controller.response.PopupResponseDto;
import com.app.toaster.popup.service.PopupService;
import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.config.UserId;
import com.app.toaster.exception.Success;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/popup")
public class PopupController {
	private final PopupService popupService;


	@PatchMapping("/{userId}")
	public ApiResponse<InvisibleResponseDto> updateInvisible(@PathVariable Long userId, @RequestBody PopUpRequestDto popUpRequestDto){
		return ApiResponse.success(Success.UPDATE_POPUP_SUCCESS, popupService.updatePopupInvisible(userId,popUpRequestDto));
	}

	@GetMapping("/{userId}")
	public ApiResponse<PopupResponseDto> getPopUpInformation(@PathVariable(name = "userId") Long userId){
		return ApiResponse.success(Success.GET_POPUP_SUCCESS, popupService.findPopupInformation(userId));
	}

}
