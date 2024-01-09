package com.app.toaster.controller.response.search;

import java.util.List;

import com.app.toaster.controller.response.toast.ToastDto;

public record SearchMainResult(List<ToastDto> toasts, List<CategoryResult> categories) {
	public static SearchMainResult of(List<ToastDto> toasts, List<CategoryResult> categories){
		return new SearchMainResult(toasts,categories);
	}
}
