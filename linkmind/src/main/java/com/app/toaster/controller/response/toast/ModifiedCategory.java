package com.app.toaster.controller.response.toast;

public record ModifiedCategory(Long categoryId) {
    public static ModifiedCategory of(Long categoryId){
        return new ModifiedCategory(categoryId);
    }
}
