package com.app.toaster.controller.request.fcm;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

@Slf4j
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FCMPushRequestDto {

    private String targetToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String body;

    private String image;

    public static FCMPushRequestDto sendTestPush(String targetToken) {

        return FCMPushRequestDto.builder()
                .targetToken(targetToken)
                .title("🍞" + PushMessage.TODAY_QNA.getTitle())
                .body(PushMessage.TODAY_QNA.getBody())
                .build();
    }
}