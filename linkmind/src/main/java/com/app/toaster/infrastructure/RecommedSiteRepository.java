package com.app.toaster.infrastructure;


import com.app.toaster.domain.RecommendSite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface RecommedSiteRepository extends JpaRepository<RecommendSite, Long> {

}
