package com.app.toaster.external.client.share_clip.request;

public record UserInfoRequestDto(
    String receiverSocialId,
    String receiverSocialType
) {
}
