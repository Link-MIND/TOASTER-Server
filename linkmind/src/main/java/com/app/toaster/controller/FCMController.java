package com.app.toaster.controller;

import com.app.toaster.controller.request.fcm.FCMPushRequestDto;
import com.app.toaster.service.fcm.FCMService;
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

    /**
     * 헤더와 바디를 직접 만들어 알림을 전송하는 테스트용 API (상대 답변 알람 전송에 사용)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> sendNotificationByToken(@RequestBody FCMPushRequestDto request) throws IOException {

        fcmService.pushAlarm(request);
        return ResponseEntity.ok().body("푸시알림 전송에 성공했습니다!");
    }
}