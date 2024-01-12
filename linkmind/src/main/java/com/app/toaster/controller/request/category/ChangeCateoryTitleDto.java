package com.app.toaster.controller.request.category;

import jakarta.validation.constraints.NotNull;

public record ChangeCateoryTitleDto(
        @NotNull
        Long categoryId,
        @NotNull
        String newTitle) {
}
