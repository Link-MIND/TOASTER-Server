package com.app.toaster.controller;

import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.external.client.sqs.SqsProducer;
import com.app.toaster.controller.request.fcm.FCMPushRequestDto;
import com.app.toaster.exception.Success;
import com.app.toaster.external.client.fcm.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
public class FCMController {

    private final FCMService fcmService;
//    private final FCMScheduler fcmScheduler;
    private final SqsProducer sqsProducer;

    /**
     * 헤더와 바디를 직접 만들어 알림을 전송하는 테스트용 API
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> sendNotificationByToken(
            @RequestBody FCMPushRequestDto request) throws IOException {

        fcmService.pushAlarm(request);
        return ResponseEntity.ok().body("푸시알림 전송에 성공했습니다!");
    }

    /**
     * 새로운 질문이 도착했음을 알리는 푸시 알림 활성화 API
     */
    @PostMapping("/qna")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse sendTopicScheduledTest(@RequestBody FCMPushRequestDto request) {
        sqsProducer.sendMessage(request);
        return ApiResponse.success(Success.PUSH_ALARM_PERIODIC_SUCCESS);
    }
}