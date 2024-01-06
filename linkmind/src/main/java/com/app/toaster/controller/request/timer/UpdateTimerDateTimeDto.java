package com.app.toaster.controller.request.timer;

import java.util.ArrayList;

public record UpdateTimerDateTimeDto(String remindTime, ArrayList<String> remindDate) {
}
