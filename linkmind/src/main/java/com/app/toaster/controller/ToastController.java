package com.app.toaster.controller;

import java.io.IOException;
import java.util.List;

import com.app.toaster.controller.request.toast.UpdateToastDto;
import com.app.toaster.controller.response.toast.ModifiedTitle;
import com.app.toaster.controller.response.toast.WeekLinkDto;
import com.app.toaster.service.link.LinkService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.app.toaster.common.dto.ApiResponse;
// import com.app.toaster.config.UserId;
import com.app.toaster.config.UserId;
import com.app.toaster.controller.request.toast.IsReadDto;
import com.app.toaster.controller.request.toast.OgRequestDto;
import com.app.toaster.controller.request.toast.SaveToastDto;
import com.app.toaster.controller.response.toast.IsReadResponse;
import com.app.toaster.exception.Success;
import com.app.toaster.service.parse.ParsingService;
import com.app.toaster.service.toast.ToastService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/toast")
@Validated
public class ToastController {
	private final ToastService toastService;
	private final ParsingService parsingService;
	private final LinkService linkService;

	@PostMapping("/og")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse getOgAdvanced(
		@RequestBody OgRequestDto ogRequestDto
	) throws IOException {
		return ApiResponse.success(Success.PARSING_OG_SUCCESS, parsingService.getOg(ogRequestDto.linkUrl()));
	}



	@PostMapping("/save")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse createToast(
		@UserId Long userId,
		@RequestBody SaveToastDto requestDto
	) {
		toastService.createToast(userId, requestDto);
		return ApiResponse.success(Success.CREATE_TOAST_SUCCESS);
	}

	@PatchMapping("/is-read")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<IsReadResponse> updateIsRead(
		@UserId Long userId,
		@RequestBody IsReadDto requestDto
	){
		return ApiResponse.success(Success.UPDATE_ISREAD_SUCCESS, toastService.readToast(userId,requestDto));
	}

	@DeleteMapping("/delete")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse deleteToast(		//나중에 softDelete로 변경
		@UserId Long userId,
		@RequestParam Long toastId
	) throws IOException {
		toastService.deleteToast(userId, toastId);
		return ApiResponse.success(Success.DELETE_TOAST_SUCCESS);
	}

	@GetMapping("/week")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<List<WeekLinkDto>> getWeekLinks(
			@UserId Long userId
	) {
		return ApiResponse.success(Success.GET_LINKS_SUCCESS, linkService.getWeekLinks());
	}

	@PatchMapping("/title")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<ModifiedTitle> modifyTitle(
		@UserId Long userId,
		@Valid @RequestBody UpdateToastDto updateToastDto
	){
		return ApiResponse.success(Success.UPDATE_TOAST_TITLE_SUCCESS, toastService.modifyTitle(userId,updateToastDto));
	}


}
