package com.app.toaster.controller.request.fcm;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PushMessage {

    // 알림
    TODAY_QNA("토스터",
            "링크가 구워지는 중");

    private String title;
    private String body;

    }