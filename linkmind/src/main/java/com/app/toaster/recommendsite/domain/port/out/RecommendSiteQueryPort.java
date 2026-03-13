package com.app.toaster.recommendsite.domain.port.out;

import com.app.toaster.recommendsite.domain.RecommendSite;

import java.util.List;

public interface RecommendSiteQueryPort {
    List<RecommendSite> findAll();
}
