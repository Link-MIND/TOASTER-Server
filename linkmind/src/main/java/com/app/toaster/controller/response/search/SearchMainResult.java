package com.app.toaster.controller.response.search;

import java.util.List;

public record SearchMainResult(List<ToastResult> toasts, List<CategoryResult> categories) {
	public static SearchMainResult of(List<ToastResult> toasts, List<CategoryResult> categories){
		return new SearchMainResult(toasts,categories);
	}
}
