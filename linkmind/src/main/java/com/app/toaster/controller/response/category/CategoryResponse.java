package com.app.toaster.controller.response.category;

import lombok.Builder;

@Builder
public record CategoryResponse(
        Long categoryId,
        String categoryTitle,
        int toastNum) {

}
