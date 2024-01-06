package com.app.toaster.controller;

import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.controller.request.timer.CreateTimerRequestDto;
import com.app.toaster.controller.request.timer.UpdateCategoryDateTimeDto;
import com.app.toaster.controller.response.timer.GetTimerResponseDto;
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
            @RequestBody CreateTimerRequestDto createTimerRequestDto
            ){
        timerService.createTimer(userId, createTimerRequestDto);
        return ApiResponse.success(Success.CREATE_TIMER_SUCCESS);
    }

    @GetMapping("/{timerId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<GetTimerResponseDto> getCategory(
            @RequestHeader("userId") Long userId,
            @PathVariable Long timerId) {

        return ApiResponse.success(Success.GET_TIMER_SUCCESS,timerService.getTimer(userId, timerId) );
    }

    @PatchMapping("/datetime/{timerId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse updateCategoryDatetime(
            @RequestHeader("userId") Long userId,
            @PathVariable Long timerId,
            @RequestBody UpdateCategoryDateTimeDto updateCategoryDateTimeDto){

        timerService.updateCategoryDatetime(userId,timerId, updateCategoryDateTimeDto);
        return ApiResponse.success(Success.UPDATE_TIMER_DATETIME_SUCCESS);
    }
}
