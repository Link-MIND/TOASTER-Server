package com.app.toaster.controller.response.timer;

import com.app.toaster.domain.Reminder;

import java.util.ArrayList;
import java.util.List;

public record WaitingTimerDto(Long timerId, String remindTime, String remindDates, Boolean isAlarm) {
    public static WaitingTimerDto of(Reminder timer, String remindTime, String remindDates) {
        return new WaitingTimerDto(timer.getId(), remindTime, remindDates, timer.getIsAlarm());
    }
}
