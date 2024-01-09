package com.app.toaster.controller.request.timer;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public record CreateTimerRequestDto(
     Long categoryId,
     String remindTime,
     ArrayList<Integer> remindDates){
}
