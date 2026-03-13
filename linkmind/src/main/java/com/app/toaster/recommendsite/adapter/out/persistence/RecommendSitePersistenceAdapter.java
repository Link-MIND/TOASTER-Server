package com.app.toaster.recommendsite.adapter.out.persistence;

import com.app.toaster.recommendsite.domain.RecommendSite;
import com.app.toaster.recommendsite.domain.port.out.RecommendSiteQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecommendSitePersistenceAdapter implements RecommendSiteQueryPort {

    private final RecommendSiteRepository recommendSiteRepository;

    @Override
    public List<RecommendSite> findAll() {
        return recommendSiteRepository.findAll();
    }
}
