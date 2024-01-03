package com.app.toaster.controller.request.toast;

import java.util.List;

import com.app.toaster.controller.valid.Severity;
import com.app.toaster.controller.valid.TitleValid;

public record SaveToastDto(String linkUrl, @TitleValid(payload = Severity.Error.class) String title, Long categoryId) {
}
