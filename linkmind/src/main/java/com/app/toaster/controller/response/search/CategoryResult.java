package com.app.toaster.controller.response.search;

public record CategoryResult(Long categoryId, String title, Long toastNum) {
	public static CategoryResult of(Long categoryId, String title, Long toastNum){
		return new CategoryResult(categoryId, title, toastNum);
	}
}
