package com.app.toaster.toast.controller.request;

import com.app.toaster.controller.valid.Severity;
import com.app.toaster.controller.valid.TitleValid;

import jakarta.validation.constraints.NotNull;

public record UpdateToastDto(Long toastId, @TitleValid(payload = Severity.Error.class) @NotNull String title) {
	public static UpdateToastDto of(Long toastId, String title){
		return new UpdateToastDto(toastId,title);
	}
}
