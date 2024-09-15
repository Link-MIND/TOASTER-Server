package com.app.toaster.popup.controller.response;

public record InvisibleResponseDto(Long popupId, boolean invisible) {
	public static InvisibleResponseDto of(Long popupId, boolean invisible){
		return new InvisibleResponseDto(popupId, invisible);
	}
}
