package com.app.toaster.controller.request.toast;

import jakarta.validation.constraints.NotNull;

public record MoveToastDto(Long toastId, @NotNull Long categoryId) {
}
