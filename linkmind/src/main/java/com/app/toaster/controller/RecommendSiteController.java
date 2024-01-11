package com.app.toaster.controller;

import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.config.UserId;
import com.app.toaster.domain.RecommendSite;
import com.app.toaster.exception.Success;
import com.app.toaster.service.recommendSite.RecommendSiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecommendSiteController {

    private final RecommendSiteService recommendSiteService;
    @GetMapping("/sites")
    public ApiResponse<List<RecommendSite>> getRecommendSites(
            @UserId Long userId){
        return ApiResponse.success(Success.GET_TIMER_SUCCESS,recommendSiteService.getRecommendSites());
    }
}
