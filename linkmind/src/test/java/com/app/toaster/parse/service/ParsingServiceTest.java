package com.app.toaster.parse.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.toaster.parse.controller.response.OgResponse;
import com.app.toaster.toast.controller.request.SaveToastDto;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParsingServiceTest {

    @InjectMocks
    private ParsingService parsingService;

    @Test
    @DisplayName("리다이렉션 Url에 대해서도 open graph 파싱이 잘된다.")
    void getOgWhenRedirect302Url() throws IOException {
        // given
        String redirectUrl = createRedirect302CaseFixture().linkUrl();

        // when - 직접 Jsoup으로 테스트
        Document doc = Jsoup.connect(redirectUrl)
            .followRedirects(true)
            .maxBodySize(1024 * 1024)
            .timeout(10000)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
            .get();

        Elements ogTitleElements = doc.select("meta[property=og:title]");
        Elements ogImageElements = doc.select("meta[property=og:image]");
        Elements titleElements = doc.select("title");

        // then - Jsoup 레벨에서 먼저 검증
        System.out.println("OG Title: " + (ogTitleElements.isEmpty() ? "없음" : ogTitleElements.attr("content")));
        System.out.println("OG Image: " + (ogImageElements.isEmpty() ? "없음" : ogImageElements.attr("content")));
        System.out.println("Title: " + (titleElements.isEmpty() ? "없음" : titleElements.text()));

        // when - 서비스 레벨 테스트
        OgResponse result = parsingService.getOg(redirectUrl);

        // then
        assertThat(result).isNotNull();
        assertThat(result.titleAdvanced()).isNotBlank();
        assertThat(result.imageAdvanced()).isNotBlank();

        // 추가 검증
        assertThat(result.titleAdvanced()).isNotEqualTo("기본 토스트 제목");
        assertThat(result.imageAdvanced()).isNotEqualTo("BASIC_THUMBNAIL_URL");
    }

    private SaveToastDto createRedirect302CaseFixture(){
        return new SaveToastDto("https://digital.mk.co.kr/news_link.php?year=2025&no=469576", 1L);
    }
}