package com.app.toaster.controller.response.search;

import java.util.List;

import com.app.toaster.domain.Category;

public record SearchCategoryResult(List<CategoryResult> categories) {
	public static SearchCategoryResult of(List<CategoryResult> categories){
		return new SearchCategoryResult(categories);
	}
}
