package com.app.toaster.toast.controller.response;

public record ModifiedCategory(Long categoryId) {
    public static ModifiedCategory of(Long categoryId){
        return new ModifiedCategory(categoryId);
    }
}
