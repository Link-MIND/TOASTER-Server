package com.app.toaster.controller.response.toast;

import com.app.toaster.domain.Link;

public record WeekSiteDto(Long linkId, String linkTitle, String toastImg, String toastLink) {
    public static WeekSiteDto of(Link link){
        return new WeekSiteDto(link.getId(), link.getTitle(), link.getThumbnailUrl(), link.getLinkUrl());
    }
}
