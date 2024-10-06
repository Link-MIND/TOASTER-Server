package com.app.toaster.toast.controller.request;

import jakarta.validation.constraints.NotNull;

public record MoveToastDto(Long toastId, @NotNull Long categoryId) {
}
