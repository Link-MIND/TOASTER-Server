package com.app.toaster.popup.controller.request;

import jakarta.validation.Valid;

public record PopUpRequestDto(
	@Valid
	Long popupId,
	@Valid
	boolean invisible
) {
}
