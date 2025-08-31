package com.app.toaster.external.client.share_clip.request;


public record CreateShareClipRequestDto(
    UserInfoRequestDto userInfoRequestDto,
    ClipInfoRequestDto clipDto
) {
}
