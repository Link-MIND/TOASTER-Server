package com.app.toaster.controller.response.search;

public record CategoryResult(Long categoryId, String title) {
	public static CategoryResult of(Long categoryId, String title){
		return new CategoryResult(categoryId, title);
	}
}
