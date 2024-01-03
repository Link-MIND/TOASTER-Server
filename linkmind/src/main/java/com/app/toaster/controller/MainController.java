package com.app.toaster.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.service.search.SearchService;
import com.app.toaster.service.toast.ToastService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {
	private final SearchService searchService;

	@GetMapping("/search")
	public ApiResponse searchProducts(@RequestHeader Long userId ,@RequestParam("query") String query){
		return searchService.searchMain(userId,query);
	}
}
