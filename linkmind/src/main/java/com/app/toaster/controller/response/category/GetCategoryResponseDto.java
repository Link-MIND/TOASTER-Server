package com.app.toaster.controller.response.category;

import com.app.toaster.controller.response.auth.TokenResponseDto;
import com.app.toaster.controller.response.toast.ToastDto;
import com.app.toaster.domain.Toast;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record GetCategoryResponseDto(
        int allToastNum,
        List<ToastDto> toastListDto
){

}
