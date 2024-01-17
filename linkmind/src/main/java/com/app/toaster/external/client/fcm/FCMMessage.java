package com.app.toaster.external.client.fcm;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FCMMessage {

    private boolean validateOnly;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private Notification notification;   // 모든 모바일 OS에 통합으로 사용할 수 있는 Notification
        private String token;   // 특정 디바이스(클라이언트)에 알림을 보내기 위한 토큰
        private String topic;   // 주제 구독 시 사용
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }

}
