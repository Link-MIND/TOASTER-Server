package com.app.toaster.config.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.app.toaster.controller.request.fcm.FCMPushRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class SqsProducer {

    @Value("${cloud.aws.sqs.notification.url}")
    private String NOTIFICATION_URL;

    private static final String GROUP_ID = "sqs";
    private final ObjectMapper objectMapper;
    private final AmazonSQS amazonSqs;
    private static final String SQS_QUEUE_REQUEST_LOG_MESSAGE = "====> [SQS Queue Request] : %s ";

    public SqsProducer(ObjectMapper objectMapper, AmazonSQS amazonSqs) {
        this.objectMapper = objectMapper;
        this.amazonSqs = amazonSqs;
    }

    public void produce(FCMPushRequestDto requestDto) {
        try {
            SendMessageRequest request = new SendMessageRequest(NOTIFICATION_URL,
                    objectMapper.writeValueAsString(requestDto))
                    .withMessageGroupId(GROUP_ID)
                    .withMessageDeduplicationId(UUID.randomUUID().toString());

            amazonSqs.sendMessage(request);
            log.info(String.format(SQS_QUEUE_REQUEST_LOG_MESSAGE, request));

        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }
}