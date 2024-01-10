package com.app.toaster.config.sqs;

import com.app.toaster.controller.request.fcm.FCMPushRequestDto;
import com.app.toaster.external.client.slack.SlackApi;
import com.app.toaster.service.fcm.FCMScheduler;
import com.app.toaster.service.fcm.FCMService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 큐 대기열에 있는 메시지 목록을 조회하여 받아오는(pull) 역할
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SqsConsumer {

    private final ObjectMapper objectMapper;

    private final FCMService fcmService;
    private static final String SQS_CONSUME_LOG_MESSAGE =
            "====> [SQS Queue Response]\n" + "info: %s\n" + "header: %s\n";


    // SQS로부터 메시지를 받는 Listener | 메시지를 받은 이후의 삭제 정책을 NEVER로 지정
    // -> 절대 삭제 요청을 보내지 않고, ack 메서드를 호출할 때 삭제 요청을 보냄
    @SqsListener(value = "${cloud.aws.sqs.notification.name}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    public void consume(@Payload String payload, Acknowledgment ack) {
        try {
                FCMPushRequestDto request = objectMapper.readValue(payload, FCMPushRequestDto.class);
                fcmService.pushAlarm(request);
                log.info(String.format(SQS_CONSUME_LOG_MESSAGE, payload));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        ack.acknowledge();
    }

}