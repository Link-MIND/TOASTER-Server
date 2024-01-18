package com.app.toaster.external.client.fcm;

import com.app.toaster.domain.Reminder;
import com.app.toaster.infrastructure.TimerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FCMScheduler {
    private final TimerRepository timerRepository;
    private final FCMService fcmService;

    private final ObjectMapper objectMapper;  // FCM의 body 형태에 따라 생성한 값을 문자열로 저장하기 위한 Mapper 클래스

    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul")
    public String pushTodayTimer()  {

        log.info("리마인드 알람");

        // 오늘 요일
        int today = LocalDateTime.now().getDayOfWeek().getValue();

        timerRepository.findAll().stream().filter(reminder -> reminder.getRemindDates().contains(today)).forEach(timer -> {
            System.out.println("timerId : " + timer.getId());
            String cronExpression = String.format("0 %s %s * * ?", timer.getRemindTime().getMinute(),timer.getRemindTime().getHour());

            fcmService.schedulePushAlarm(cronExpression, timer.getId());

        });

        return "오늘의 토스터를 구워 전달했어요!!!";
    }


}
