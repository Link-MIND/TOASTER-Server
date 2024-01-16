package com.app.toaster.controller.request.category;

import com.app.toaster.controller.valid.Severity;
import com.app.toaster.controller.valid.TitleValid;
import jakarta.validation.constraints.NotNull;

public record ChangeCateoryTitleDto(
        @NotNull
        Long categoryId,
        @TitleValid(payload = Severity.Error.class) @NotNull String newTitle) {
}
