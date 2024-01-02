package com.app.toaster.service.search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.controller.response.search.CategoryResult;
import com.app.toaster.controller.response.search.SearchCategoryResult;
import com.app.toaster.domain.Category;
import com.app.toaster.domain.User;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.Success;
import com.app.toaster.exception.model.CustomException;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.infrastructure.CategoryRepository;
import com.app.toaster.infrastructure.ToastRepository;
import com.app.toaster.infrastructure.UserRepository;
import com.google.protobuf.Api;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	private final ToastRepository toastRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;

	public ApiResponse<SearchCategoryResult> searchCategorytitle(Long userId, String searchParam){
		User presentUser =  userRepository.findByUserId(userId).orElseThrow(
			()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
		);
		List<Category> searchCategoryList = categoryRepository.searchCategoriesByQuery(userId, searchParam);

		if (searchCategoryList.isEmpty()){
			return ApiResponse.success(Success.SEARCH_SUCCESS_BUT_IS_EMPTY,null);
		}

		return ApiResponse.success(Success.SEARCH_SUCCESS, SearchCategoryResult.of(
			searchCategoryList.stream().map(
				category -> CategoryResult.of(category.getCategoryId(), category.getTitle()))
				.collect(Collectors.toList())));
	}

}
