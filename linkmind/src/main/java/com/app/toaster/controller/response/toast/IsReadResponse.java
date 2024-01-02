package com.app.toaster.controller.response.toast;

public record IsReadResponse(Boolean isRead) {
	public static IsReadResponse of(Boolean isRead){
		return new IsReadResponse(isRead);
	}
}
