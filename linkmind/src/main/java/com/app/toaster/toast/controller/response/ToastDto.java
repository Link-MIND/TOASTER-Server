package com.app.toaster.toast.controller.response;

import com.app.toaster.toast.domain.Toast;

public record ToastDto (Long toastId, String toastTitle, String linkUrl, Boolean isRead, String categoryTitle, String thumbnailUrl){
    public static ToastDto of(Toast toast){
        return new ToastDto(toast.getId(),toast.getTitle(), toast.getLinkUrl(), toast.getIsRead(), toast.getCategory() == null ? null : toast.getCategory().getTitle(),
            toast.getThumbnailUrl());
    }
}
