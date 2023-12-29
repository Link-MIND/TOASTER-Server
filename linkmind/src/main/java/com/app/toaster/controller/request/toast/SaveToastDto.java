package com.app.toaster.controller.request.toast;

import java.util.List;

public record SaveToastDto(String linkUrl, String title, List<Long> categoryIds) {
}
