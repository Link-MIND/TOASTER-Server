package com.app.toaster.external.client.fcm;

import com.app.toaster.domain.Reminder;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Random;

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

    public static FCMPushRequestDto sendTestPush(String targetToken, String comment) {

        return FCMPushRequestDto.builder()
                .targetToken(targetToken)
                .title("🍞 토스트 ")
                .body(comment)
                .build();

    }

}