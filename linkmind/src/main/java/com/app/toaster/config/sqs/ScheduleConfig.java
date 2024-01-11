package com.app.toaster.config.sqs;

import com.app.toaster.service.fcm.FCMService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 특정 시간대에 알림을 보내주기 위해 Spring이 제공하는 TaskScheduler를 빈으로 등록
 */
@Configuration
@EnableScheduling
public class ScheduleConfig {

    private static final int POOL_SIZE = 10;
    private static ThreadPoolTaskScheduler scheduler;


    @Bean
    public TaskScheduler scheduler() {
        scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(POOL_SIZE);
        // 스레드 이름 접두사 설정
        scheduler.setThreadNamePrefix("현재 쓰레드 풀-");
        // 거부된 실행 핸들러 설정
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        scheduler.initialize();
        return scheduler;
    }

    // 스케줄러 중지 후 재시작 (초기화)
    public static void resetScheduler() {
        scheduler.shutdown();
        FCMService.clearScheduledTasks();
        scheduler.setPoolSize(POOL_SIZE);
        scheduler.setThreadNamePrefix("현재 쓰레드 풀-");
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        scheduler.initialize();
    }



    // 단일 스레드로 예약된 작업을 처리하고자 할 때 사용
    /*@Bean
    public TaskScheduler scheduler() {
        return new ConcurrentTaskScheduler();
    }*/
}
