package com.app.toaster.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String title;

    private String linkUrl;

    private String thumbnailUrl;

    @Builder
    public Link(String title, String linkUrl, String thumbnailUrl) {
        this.title = title;
        this.linkUrl = linkUrl;
        this.thumbnailUrl = thumbnailUrl;
    }
}
