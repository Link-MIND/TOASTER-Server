package com.app.toaster.service.recommendSite;

import com.app.toaster.domain.RecommendSite;
import com.app.toaster.infrastructure.RecommedSiteRepository;
import com.app.toaster.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendSiteService {
    private final UserRepository userRepository;
    private final RecommedSiteRepository recommedSiteRepository;

    public List<RecommendSite> getRecommendSites(){

        return recommedSiteRepository.findAll().subList(0, Math.min(9, recommedSiteRepository.findAll().size()));
    }
}
