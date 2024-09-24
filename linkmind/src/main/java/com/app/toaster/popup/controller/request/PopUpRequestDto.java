package com.app.toaster.popup.controller.request;

import jakarta.validation.constraints.NotNull;

public record PopUpRequestDto(
	Long popupId,
	@NotNull
	Long hideDate
) {
}
