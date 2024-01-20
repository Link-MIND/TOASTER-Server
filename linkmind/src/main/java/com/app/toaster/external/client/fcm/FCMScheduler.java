package com.app.toaster.external.client.fcm;

import com.app.toaster.domain.Category;
import com.app.toaster.domain.Reminder;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.external.client.sqs.SqsProducer;
import com.app.toaster.infrastructure.TimerRepository;
import com.app.toaster.infrastructure.ToastRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PessimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class FCMScheduler {
    private final TimerRepository timerRepository;
    private final FCMService fcmService;

    private final ObjectMapper objectMapper;  // FCM의 body 형태에 따라 생성한 값을 문자열로 저장하기 위한 Mapper 클래스

    private final ToastRepository toastRepository;

    private final PlatformTransactionManager transactionManager;
    private final SqsProducer sqsProducer;

    @PersistenceContext
    private EntityManager em;


    private final int PUSH_MESSAGE_NUMBER = 5;
    @Scheduled(cron = "1 * * * * *", zone = "Asia/Seoul")
    public String pushTodayTimer()  {

        log.info("리마인드 알람");

        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);

        try {

            // 오늘 요일
            int today = LocalDateTime.now().getDayOfWeek().getValue();


            timerRepository.findAll().stream().filter(reminder -> reminder.getRemindDates().contains(today)).forEach(timer -> {

                LocalTime now = LocalTime.now();

                //한국 시간대로 변환
                ZoneId koreaTimeZone = ZoneId.of("Asia/Seoul");
                ZonedDateTime koreaTime = now.atDate(ZonedDateTime.now().toLocalDate()).atZone(koreaTimeZone);

                //ZonedDateTime에서 LocalTime 추출
                LocalTime koreaLocalTime = koreaTime.toLocalTime();

                // 현재 알람이 커져있고 설정값이 동일하면 알람 전송
                if(timer.getIsAlarm() && timer.getUser().getFcmIsAllowed() && timer.getRemindTime().getHour()== koreaLocalTime.getHour()
                &&timer.getRemindTime().getMinute()== koreaLocalTime.getMinute()) {
                    System.out.println("=========================================");
                    System.out.println("timer.getRemindTime().equals(koreaTime)");
                    System.out.println("================= 전송시간 =================");
                    //sqs 푸시
                    FCMPushRequestDto request = getPushMessage(timer,toastRepository.getUnReadToastNumber(timer.getUser().getUserId()) );

                    sqsProducer.sendMessage(request, timer.getId().toString());

                    System.out.println("========="+request.getTitle() + request.getBody()+"=========");

                }

            });


        } catch (PessimisticLockingFailureException | PessimisticLockException e) {
            transactionManager.rollback(transactionStatus);
        } finally {
            em.close();
        }

        return "오늘의 토스터를 구워 전달했어요!!!";
    }


    private FCMPushRequestDto getPushMessage(Reminder reminder, int unReadToastNumber){
        Random random = new Random();
        int randomNumber = random.nextInt(PUSH_MESSAGE_NUMBER);
        String categoryTitle = "전체";

        Category category = timerRepository.findCategoryByReminderId(reminder.getId());
        if(category != null){
            categoryTitle = category.getTitle();
        }


        String title="";
        String body="";

        switch (randomNumber) {
            case 0 -> {
                title = reminder.getUser().getNickname()+PushMessage.ALARM_MESSAGE_0.getTitle();
                body = categoryTitle+PushMessage.ALARM_MESSAGE_0.getBody();
            }
            case 1 -> {
                title = "띵동! " + categoryTitle+PushMessage.ALARM_MESSAGE_1.getTitle();
                body = PushMessage.ALARM_MESSAGE_1.getBody();
            }
            case 2 -> {
                title = reminder.getUser().getNickname()+"님, "+categoryTitle+PushMessage.ALARM_MESSAGE_2.getTitle();
                body = PushMessage.ALARM_MESSAGE_2.getBody();
            }
            case 3 -> {
                LocalDateTime now = LocalDateTime.now();

                title =  now.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA)+"요일 "+now.getHour()+"시에는 "
                        +categoryTitle+PushMessage.ALARM_MESSAGE_3.getTitle();
                body = PushMessage.ALARM_MESSAGE_3.getBody();
            }
            case 4 -> {
                title = reminder.getUser().getNickname()+"님, " +categoryTitle+PushMessage.ALARM_MESSAGE_4.getTitle();
                body = PushMessage.ALARM_MESSAGE_4.getBody()+unReadToastNumber+"개 남아있어요";
            }
        };

        return FCMPushRequestDto.builder()
                .targetToken(reminder.getUser().getFcmToken())
                .title(title)
                .body(body)
                .build();
    }


}
