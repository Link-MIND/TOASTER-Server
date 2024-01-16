package com.app.toaster.external.client.fcm;

import com.app.toaster.domain.Reminder;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FCMPushRequestDto {

    private final int PUSH_MESSAGE_NUMBER = 5;

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

    private void getPushMessage(Reminder reminder){
        Random random = new Random();
        int randomNumber = random.nextInt(PUSH_MESSAGE_NUMBER);

        switch (randomNumber) {
            case 0 -> {

            }
            case 1 -> {

            }
            case 2 -> {

            }
            case 3 -> {

            }
            case 4 -> {

            }
        };

    }

}