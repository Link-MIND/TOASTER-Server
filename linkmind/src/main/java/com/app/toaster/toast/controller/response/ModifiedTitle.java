package com.app.toaster.toast.controller.response;

public record ModifiedTitle(String updatedTitle) {
	public static ModifiedTitle of(String updatedTitle){
		return new ModifiedTitle(updatedTitle);
	}
}
