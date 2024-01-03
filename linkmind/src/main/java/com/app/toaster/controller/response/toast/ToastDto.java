package com.app.toaster.controller.response.toast;

import com.app.toaster.controller.response.category.GetCategoryResponseDto;
import com.app.toaster.domain.Toast;

public record ToastDto (String toastTitle, String linkUrl, Boolean isRead){
    public static ToastDto of(Toast toast){
        return new ToastDto(toast.getTitle(), toast.getLinkUrl(), toast.getIsRead());
    }
}
