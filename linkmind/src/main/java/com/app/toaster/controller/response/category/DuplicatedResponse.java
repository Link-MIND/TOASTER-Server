package com.app.toaster.controller.response.category;

public record DuplicatedResponse(Boolean isDupicated) {
	public static DuplicatedResponse of(Boolean isDupicated){
		return new DuplicatedResponse(isDupicated);
	}
}
