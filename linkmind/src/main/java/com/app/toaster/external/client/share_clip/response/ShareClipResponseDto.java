package com.app.toaster.external.client.share_clip.response;

public record ShareClipResponseDto(
    String result,
    Long clipId,
    Long userId
) {
    public static ShareClipResponseDto success(Long userId, Long clipId){
        return new ShareClipResponseDto("Y", userId, clipId);
    }

    public static ShareClipResponseDto fail(String message){
        return new ShareClipResponseDto(message, null, null);
    }
}
