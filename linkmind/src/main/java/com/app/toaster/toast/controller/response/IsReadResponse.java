package com.app.toaster.toast.controller.response;

public record IsReadResponse(Boolean isRead) {
	public static IsReadResponse of(Boolean isRead){
		return new IsReadResponse(isRead);
	}
}
