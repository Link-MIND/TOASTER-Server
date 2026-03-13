package com.app.toaster.recommendsite.application;

import com.app.toaster.recommendsite.domain.RecommendSite;
import com.app.toaster.recommendsite.domain.port.out.RecommendSiteQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendSiteService implements RecommendSiteUseCase {

    private static final int MAX_RECOMMEND_SITE_SIZE = 9;

    private final RecommendSiteQueryPort recommendSiteQueryPort;

    @Override
    public List<RecommendSite> getRecommendSites() {
        List<RecommendSite> recommendSites = recommendSiteQueryPort.findAll();
        return recommendSites.subList(0, Math.min(MAX_RECOMMEND_SITE_SIZE, recommendSites.size()));
    }
}
