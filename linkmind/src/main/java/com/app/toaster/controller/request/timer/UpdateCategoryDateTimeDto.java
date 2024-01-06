package com.app.toaster.controller.request.timer;

import java.util.ArrayList;

public record UpdateCategoryDateTimeDto(String remindTime, ArrayList<String> remindDate) {
}
