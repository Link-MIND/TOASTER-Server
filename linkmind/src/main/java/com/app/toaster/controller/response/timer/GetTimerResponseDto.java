package com.app.toaster.controller.response.timer;

import com.app.toaster.controller.response.search.CategoryResult;
import com.app.toaster.controller.response.search.SearchMainResult;
import com.app.toaster.controller.response.search.ToastResult;
import com.app.toaster.domain.Reminder;

import java.util.ArrayList;
import java.util.List;

public record GetTimerResponseDto (String categoryName,
                                       String remindTime,
                                       ArrayList<String> remindDates) {
    public static GetTimerResponseDto of(Reminder reminder){
        return new GetTimerResponseDto(reminder.getCategory().getTitle(), reminder.getRemindTime().toString(), reminder.getRemindDates());
    }
}
