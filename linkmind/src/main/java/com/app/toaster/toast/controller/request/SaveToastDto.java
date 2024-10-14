package com.app.toaster.toast.controller.request;

import java.util.List;

import com.app.toaster.controller.valid.Severity;
import com.app.toaster.controller.valid.TitleValid;

public record SaveToastDto(String linkUrl, Long categoryId) {
}
