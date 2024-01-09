package com.app.toaster.controller.request.category;

import com.app.toaster.controller.valid.Severity;
import com.app.toaster.controller.valid.TitleValid;

public record CreateCategoryDto(@TitleValid(payload = Severity.Error.class) String categoryTitle) {
}
