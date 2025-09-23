package com.app.toaster.external.client.share_clip.request;

public record ShareToastInfoRequestDto(
    String title,
    String thumbnail,
    String linkUrl
) {
}
