package com.app.toaster.controller.response.category;

import lombok.Builder;

@Builder
public record CategoriesReponse(Long CategoryId, String categoryTitle, int toastNum) {

}
