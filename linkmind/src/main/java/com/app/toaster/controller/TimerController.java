package com.app.toaster.controller;

import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.controller.request.category.CreateCategoryDto;
import com.app.toaster.controller.request.timer.CreateTimerRequestDto;
import com.app.toaster.exception.Success;
import com.app.toaster.service.Timer.TimerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timer")
public class TimerController {

    private final TimerService timerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createCateory(
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody CreateTimerRequestDto createTimerRequestDto
            ){
        timerService.createTimer(userId, createTimerRequestDto);
        return ApiResponse.success(Success.CREATE_TIMER_SUCCESS);
    }
}
