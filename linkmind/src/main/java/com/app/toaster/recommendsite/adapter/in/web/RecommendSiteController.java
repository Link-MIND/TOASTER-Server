package com.app.toaster.recommendsite.adapter.in.web;

import com.app.toaster.common.config.UserId;
import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.exception.Success;
import com.app.toaster.recommendsite.application.RecommendSiteUseCase;
import com.app.toaster.recommendsite.domain.RecommendSite;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecommendSiteController {

    private final RecommendSiteUseCase recommendSiteUseCase;

    @GetMapping("/sites")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<RecommendSite>> getRecommendSites(@UserId Long userId) {
        return ApiResponse.success(Success.GET_SETTINGS_SUCCESS, recommendSiteUseCase.getRecommendSites());
    }
}
