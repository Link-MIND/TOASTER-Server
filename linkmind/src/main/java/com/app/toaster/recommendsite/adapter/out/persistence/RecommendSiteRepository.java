package com.app.toaster.recommendsite.adapter.out.persistence;

import com.app.toaster.recommendsite.domain.RecommendSite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendSiteRepository extends JpaRepository<RecommendSite, Long> {
}
