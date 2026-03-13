package com.app.toaster.recommendsite.application;

import com.app.toaster.recommendsite.domain.RecommendSite;
import com.app.toaster.recommendsite.domain.Topic;
import com.app.toaster.recommendsite.domain.port.out.RecommendSiteQueryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RecommendSiteServiceTest {

    @InjectMocks
    private RecommendSiteService recommendSiteService;

    @Mock
    private RecommendSiteQueryPort recommendSiteQueryPort;

    @Test
    @DisplayName("추천 사이트가 9개보다 많으면 9개만 반환한다")
    void shouldReturnAtMostNineRecommendSites() {
        // given
        List<RecommendSite> allSites = java.util.stream.IntStream.range(0, 12)
                .mapToObj(this::buildSite)
                .toList();
        given(recommendSiteQueryPort.findAll()).willReturn(allSites);

        // when
        List<RecommendSite> result = recommendSiteService.getRecommendSites();

        // then
        assertThat(result).hasSize(9);
        assertThat(result).containsExactlyElementsOf(allSites.subList(0, 9));
    }

    @Test
    @DisplayName("추천 사이트가 9개 이하면 전체를 반환한다")
    void shouldReturnAllRecommendSitesWhenSizeIsLessThanNine() {
        // given
        List<RecommendSite> allSites = java.util.stream.IntStream.range(0, 4)
                .mapToObj(this::buildSite)
                .toList();
        given(recommendSiteQueryPort.findAll()).willReturn(allSites);

        // when
        List<RecommendSite> result = recommendSiteService.getRecommendSites();

        // then
        assertThat(result).hasSize(4);
        assertThat(result).containsExactlyElementsOf(allSites);
    }

    private RecommendSite buildSite(int index) {
        return RecommendSite.builder()
                .siteTitle("site-" + index)
                .siteUrl("https://example.com/" + index)
                .siteImg("https://example.com/image-" + index + ".png")
                .siteSub(Topic.IT)
                .build();
    }
}
