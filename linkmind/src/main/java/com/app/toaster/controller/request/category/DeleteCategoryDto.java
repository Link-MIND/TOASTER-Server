package com.app.toaster.controller.request.category;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public record DeleteCategoryDto(@NotNull ArrayList<Long> deleteCategoryList) {
}
