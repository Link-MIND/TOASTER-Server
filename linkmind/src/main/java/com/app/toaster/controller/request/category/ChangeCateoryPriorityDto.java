package com.app.toaster.controller.request.category;

import jakarta.validation.constraints.NotNull;

public record ChangeCateoryPriorityDto(
        @NotNull
        Long categoryId,
        @NotNull
        int newPriority) {
}
