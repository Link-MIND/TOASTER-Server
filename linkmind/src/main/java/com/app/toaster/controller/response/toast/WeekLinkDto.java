package com.app.toaster.controller.response.toast;

import com.app.toaster.domain.Link;

public record WeekLinkDto(Long linkId, String linkTitle, String linkImg, String linkUrl) {
    public static WeekLinkDto of(Link link){
        return new WeekLinkDto(link.getId(), link.getTitle(), link.getThumbnailUrl(), link.getLinkUrl());
    }
}
