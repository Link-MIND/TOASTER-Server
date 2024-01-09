package com.app.toaster.controller.response.timer;

import com.app.toaster.controller.response.search.CategoryResult;
import com.app.toaster.controller.response.search.SearchCategoryResult;
import com.app.toaster.domain.Reminder;

import java.util.List;

public record CompletedTimerDto(Long timerId, Long categoryId, String remindTime, String remindDate, String comment) {
    public static CompletedTimerDto of(Reminder timer,String remindTime, String remindDate){
        return new CompletedTimerDto(timer.getId(), timer.getCategory().getCategoryId(), remindTime, remindDate, timer.getComment() );
    }
}
