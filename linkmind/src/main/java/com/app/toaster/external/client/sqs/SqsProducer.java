package com.app.toaster.external.client.sqs;

import com.app.toaster.controller.request.fcm.FCMPushRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsProducer {

    @Value("${cloud.aws.sqs.notification.name}")
    private String QUEUE_NAME;

    private static final String GROUP_ID = "sqs";
    private final ObjectMapper objectMapper;
    private final SqsTemplate template;
    private static final String SQS_QUEUE_REQUEST_LOG_MESSAGE = "====> [SQS Queue Request] : %s ";


    public void sendMessage(FCMPushRequestDto requestDto) {
        System.out.println("Sender: " + requestDto.getBody());
        template.send(to -> {
            try {
                to.queue(QUEUE_NAME)
                        .messageGroupId(GROUP_ID)
                        .payload(objectMapper.writeValueAsString(requestDto));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}