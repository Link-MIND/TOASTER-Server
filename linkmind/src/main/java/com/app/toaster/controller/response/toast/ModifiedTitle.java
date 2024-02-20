package com.app.toaster.controller.response.toast;

public record ModifiedTitle(String updatedTitle) {
	public static ModifiedTitle of(String updatedTitle){
		return new ModifiedTitle(updatedTitle);
	}
}
