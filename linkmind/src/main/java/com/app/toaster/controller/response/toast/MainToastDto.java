package com.app.toaster.controller.response.toast;

import com.app.toaster.domain.Toast;

public record MainToastDto(Long toastId, String toastTitle, String toastImg, String toastLink) {
    public static MainToastDto of(Toast toast){
        return new MainToastDto(toast.getId(), toast.getTitle(), toast.getThumbnailUrl(), toast.getLinkUrl());
    }
}
