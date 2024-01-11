package com.app.toaster.controller.response.toast;

import com.app.toaster.domain.Toast;

public record WeekSiteDto(Long toastId, String toastTitle, String toastImg, String toastLink) {
    public static WeekSiteDto of(Toast toast){
        return new WeekSiteDto(toast.getId(), toast.getTitle(), toast.getThumbnailUrl(), toast.getLinkUrl());
    }
}
