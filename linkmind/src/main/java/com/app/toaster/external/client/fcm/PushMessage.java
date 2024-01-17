package com.app.toaster.external.client.fcm;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PushMessage {

    ALARM_MESSAGE_0("님, 타이머가 완료되었어요!",
            " 클립 읽기 딱 좋은 시간이에요."),

    ALARM_MESSAGE_1(" 클립이 다 구워졌어요.",
            "링크가 타기 전에 읽어보세요🔗"),

    ALARM_MESSAGE_2(" 클립을 읽을 시간이에요!",
            "타이머에서 확인해보세요⏱️"),

    ALARM_MESSAGE_3(" 클립을 읽어보세요!",
            " 토스터 읽기 좋은 시간이에요🍞"),

    ALARM_MESSAGE_4(" 클립 읽을 시간 리마인드 드려요!",
                            " 아직 못 읽은 링크가 ");


    private String title;
    private String body;


}
