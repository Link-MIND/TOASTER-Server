package com.app.toaster.controller.response.category;

import com.app.toaster.toast.controller.response.ToastDto;
import lombok.Builder;

import java.util.List;

@Builder
public record GetCategoryResponseDto(
        int allToastNum,
        List<ToastDto> toastListDto
){

}
