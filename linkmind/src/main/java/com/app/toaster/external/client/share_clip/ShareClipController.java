package com.app.toaster.external.client.share_clip;

import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.exception.Success;
import com.app.toaster.external.client.share_clip.request.CreateShareClipRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/share-clip")
public class ShareClipController {

    private final ShareClipService shareClipService;

    @PostMapping
    public ApiResponse<?> createShareClip(@RequestBody CreateShareClipRequestDto createShareClipRequestDto){
        return ApiResponse.success(Success.CREATE_SHARE_CLIP, shareClipService.createShareClip(createShareClipRequestDto));
    }
}
