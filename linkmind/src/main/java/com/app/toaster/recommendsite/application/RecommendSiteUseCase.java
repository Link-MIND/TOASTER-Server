package com.app.toaster.recommendsite.application;

import com.app.toaster.recommendsite.domain.RecommendSite;

import java.util.List;

public interface RecommendSiteUseCase {
    List<RecommendSite> getRecommendSites();
}
