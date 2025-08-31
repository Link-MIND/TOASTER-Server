package com.app.toaster.external.client.share_clip.request;

import java.util.List;

public record ClipInfoRequestDto(
    String clipTitle,
    List<ShareToastInfoRequestDto> toasts
) {
}
