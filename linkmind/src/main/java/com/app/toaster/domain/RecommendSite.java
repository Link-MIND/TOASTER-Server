package com.app.toaster.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long siteId;

    String siteTitle;

    String siteUrl;

    String siteImg;

    Enum siteSub;


    @Builder
    public RecommendSite(String siteTitle, String siteUrl, String siteImg, Enum siteSub) {
        this.siteTitle =siteTitle;
        this.siteUrl=siteUrl;
        this.siteImg = siteImg;
        this.siteSub =siteSub;
    }



}
