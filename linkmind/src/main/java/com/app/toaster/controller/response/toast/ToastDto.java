package com.app.toaster.controller.response.toast;

import com.app.toaster.controller.response.category.GetCategoryResponseDto;
import com.app.toaster.domain.Toast;

public record ToastDto (Long toastId, String toastTitle, String linkUrl, Boolean isRead, String categoryTitle, String thumbnailUrl){
    public static ToastDto of(Toast toast){
        return new ToastDto(toast.getId(),toast.getTitle(), toast.getLinkUrl(), toast.getIsRead(), toast.getCategory() == null ? null : toast.getCategory().getTitle(),
            toast.getThumbnailUrl());
    }
}
