package com.app.toaster.external.client.fcm;

import com.app.toaster.external.client.sqs.SqsProducer;
import com.app.toaster.domain.Reminder;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.infrastructure.TimerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PessimisticLockException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.scheduling.TaskScheduler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;


@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {
    private final TimerRepository timerRepository;

    private final ObjectMapper objectMapper;  // FCM의 body 형태에 따라 생성한 값을 문자열로 저장하기 위한 Mapper 클래스

    @Value("${fcm.key.path}")
    private String SERVICE_ACCOUNT_JSON;
    @Value("${fcm.api.url}")
    private String FCM_API_URL;
    @Value("${fcm.topic}")
    private String topic;


    private static ScheduledFuture<?> scheduledFuture;
    private final TaskScheduler taskScheduler;
    private final PlatformTransactionManager transactionManager;
    private final SqsProducer sqsProducer;

    @PersistenceContext
    private EntityManager em;

    /**
     * 단일 기기
     * - Firebase에 메시지를 수신하는 함수 (헤더와 바디 직접 만들기)
     */
    @Transactional
    public String pushAlarm(FCMPushRequestDto request) throws IOException {

        String message = makeSingleMessage(request);
        sendPushMessage(message);
        return "알림을 성공적으로 전송했습니다. targetUserId = " + request.getTargetToken();
    }

    // 요청 파라미터를 FCM의 body 형태로 만들어주는 메서드 [단일 기기]
    private String makeSingleMessage(FCMPushRequestDto request) throws JsonProcessingException {

        FCMMessage fcmMessage = FCMMessage.builder()
                .message(FCMMessage.Message.builder()
                        .token(request.getTargetToken())   // 1:1 전송 시 반드시 필요한 대상 토큰 설정
                        .notification(FCMMessage.Notification.builder()
                                .title(request.getTitle())
                                .body(request.getBody())
                                .image(request.getImage())
                                .build())
                        .build()
                ).validateOnly(false)
                .build();

        return objectMapper.writeValueAsString(fcmMessage);
    }


    // 실제 파이어베이스 서버로 푸시 메시지를 전송하는 메서드
    private void sendPushMessage(String message) throws IOException {

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request httpRequest = new Request.Builder()
                .url(FCM_API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(httpRequest).execute();

        log.info("단일 기기 알림 전송 성공 ! successCount: 1 messages were sent successfully");
        log.info("알림 전송: {}", response.body().string());
    }

    // Firebase에서 Access Token 가져오기
    private String getAccessToken() throws IOException {

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(SERVICE_ACCOUNT_JSON).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        log.info("getAccessToken() - googleCredentials: {} ", googleCredentials.getAccessToken().getTokenValue());

        return googleCredentials.getAccessToken().getTokenValue();
    }

    // 푸시알림 스케줄러
    public void schedulePushAlarm(String cronExpression,Long timerId) {

        scheduledFuture = taskScheduler.schedule(() -> {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
            TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);

            try {

                Reminder timer = timerRepository.findById(timerId).orElseThrow(
                        ()-> new NotFoundException(Error.NOT_FOUND_TIMER, Error.NOT_FOUND_TIMER.getMessage())
                );

                String cron = String.format("0 %s %s * * ?", timer.getRemindTime().getMinute(),timer.getRemindTime().getHour());

                // 현재 알람이 커져있고 설정값이 동일하면 알람 전송
                if(timer.getIsAlarm() && timer.getUser().getFcmIsAllowed() && cronExpression.equals(cron)) {
                    System.out.println("================= 전송시간 =================");
                    //sqs 푸시
                    FCMPushRequestDto request = FCMPushRequestDto.builder().title("🍞토스터 🍞")
                            .targetToken(timer.getUser().getFcmToken())
                            .body(timer.getComment()).image("empty").build();

                    sqsProducer.sendMessage(request);

                }

            } catch (PessimisticLockingFailureException | PessimisticLockException e) {
                transactionManager.rollback(transactionStatus);
            } finally {
                em.close();
            }


        }, new CronTrigger(cronExpression));

    }

    // 스케줄러에서 예약된 작업을 제거하는 메서드
    public static void clearScheduledTasks() {
        if (scheduledFuture != null) {
            log.info("이전 스케줄링 예약 취소!");
            scheduledFuture.cancel(false);
        }
        log.info("ScheduledFuture: {}", scheduledFuture);
    }
}