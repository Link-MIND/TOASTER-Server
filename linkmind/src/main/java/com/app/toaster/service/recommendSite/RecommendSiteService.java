package com.app.toaster.service.recommendSite;

import com.app.toaster.controller.response.category.CategoriesReponse;
import com.app.toaster.controller.response.main.MainPageResponseDto;
import com.app.toaster.controller.response.toast.MainToastDto;
import com.app.toaster.domain.RecommendSite;
import com.app.toaster.domain.User;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.infrastructure.RecommedSiteRepository;
import com.app.toaster.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendSiteService {
    private final UserRepository userRepository;
    private final RecommedSiteRepository recommedSiteRepository;

    public List<RecommendSite> getRecommendSites(){

        return recommedSiteRepository.findAll().subList(0, Math.min(9, recommedSiteRepository.findAll().size()));
    }
}
