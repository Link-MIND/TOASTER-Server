package com.app.toaster.controller.response.main;

import com.app.toaster.controller.response.category.CategoriesReponse;
import com.app.toaster.domain.RecommendSite;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record MainPageResponseDto(String nickname, int readToastNum, int allToastNum, List<CategoriesReponse> mainCategoryListDto, List<RecommendSite> recommendedSiteListDto) {
}
