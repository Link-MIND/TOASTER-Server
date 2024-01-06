package com.app.toaster.controller.response.timer;

import lombok.Builder;

import java.util.List;

@Builder
public record GetTimerPageResponseDto(List<CompletedTimerDto> completedTimerList, List<WaitingTimerDto> waitingTimerList) {
}
