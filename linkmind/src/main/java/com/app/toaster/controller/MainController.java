package com.app.toaster.controller;

import com.app.toaster.config.UserId;
import com.app.toaster.controller.response.main.MainPageResponseDto;
import com.app.toaster.controller.response.timer.GetTimerResponseDto;
import com.app.toaster.exception.Success;
import com.app.toaster.service.main.MainService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.config.UserId;
import com.app.toaster.service.UserService;
import com.app.toaster.service.search.SearchService;
import com.app.toaster.service.toast.ToastService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {
	private final SearchService searchService;
	private final MainService mainService;

	private final UserService userService;

	@GetMapping("/search")
	public ApiResponse searchProducts(@UserId Long userId ,@RequestParam("query") String query){
		return searchService.searchMain(userId,query);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<MainPageResponseDto> getTimer(
			@UserId Long userId) {

		return ApiResponse.success(Success.GET_TIMER_SUCCESS,mainService.getMainPage(userId) );
	}
}
