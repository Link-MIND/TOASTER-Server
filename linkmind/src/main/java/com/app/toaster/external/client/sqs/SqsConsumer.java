package com.app.toaster.external.client.sqs;

import com.app.toaster.controller.request.fcm.FCMPushRequestDto;
import com.app.toaster.external.client.fcm.FCMService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

import java.util.Map;

/**
 * 큐 대기열에 있는 메시지 목록을 조회하여 받아오는(pull) 역할
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SqsConsumer {

    @Value("${cloud.aws.sqs.notification.url}")
    private String QUEUE_URL;

    private final ObjectMapper objectMapper;

    private final FCMService fcmService;
    private final SqsAsyncClient sqsAsyncClient;
    private static final String SQS_CONSUME_LOG_MESSAGE =
            "====> [SQS Queue Response]\n" + "info: %s\n" + "header: %s\n";


//     SQS로부터 메시지를 받는 Listener | 메시지를 받은 이후의 삭제 정책을 NEVER로 지정
//     -> 절대 삭제 요청을 보내지 않고, ack 메서드를 호출할 때 삭제 요청을 보냄
    @SqsListener(value = "${cloud.aws.sqs.notification.name}")
    public void consume(@Payload String payload, @Headers Map<String, String> headers) {
        System.out.println("======== 수신 받음 ==============");

//        log.info(headers.toString());

        try {
                FCMPushRequestDto request = objectMapper.readValue(payload, FCMPushRequestDto.class);
                fcmService.pushAlarm(request);

//                log.info(SQS_CONSUME_LOG_MESSAGE + payload);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    private void deleteMessage(String receiptHandle) {
        System.out.println("========== deleteMessage =========");
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .receiptHandle(receiptHandle)
                .build();
        sqsAsyncClient.deleteMessage(deleteMessageRequest);
    }


}