package com.app.toaster.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.domain.Category;
import com.app.toaster.infrastructure.CategoryRepository;
import com.app.toaster.service.search.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
	private final SearchService searchService;

	@GetMapping("/search")
	public ApiResponse searchProducts(@RequestHeader Long userId ,@RequestParam("query") String query){
		return searchService.searchCategorytitle(userId,query);
	}
}
