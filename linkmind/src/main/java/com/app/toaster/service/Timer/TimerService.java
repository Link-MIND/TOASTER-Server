package com.app.toaster.service.Timer;

import com.app.toaster.controller.request.timer.CreateTimerRequestDto;
import com.app.toaster.domain.Category;
import com.app.toaster.domain.Reminder;
import com.app.toaster.domain.User;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.infrastructure.CategoryRepository;
import com.app.toaster.infrastructure.TimerRepository;
import com.app.toaster.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TimerService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TimerRepository timerRepository;

    public void createTimer(Long userId, CreateTimerRequestDto createTimerRequestDto){
        User presentUser = userRepository.findByUserId(userId).orElseThrow(
                ()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));

        Category category = categoryRepository.findById(createTimerRequestDto.categoryId())
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_CATEGORY_EXCEPTION, Error.NOT_FOUND_CATEGORY_EXCEPTION.getMessage()));


        Reminder reminder = Reminder.builder()
                .user(presentUser)
                .category(category)
                .remindDates(createTimerRequestDto.remindDates())
                .remindTime(LocalTime.parse(createTimerRequestDto.remindTime()))
                .isAlarm(true)
                .comment(category.getTitle() + " 링크들을 읽기 딱 좋은 시간이에요!")
                .build();


        timerRepository.save(reminder);
    }
}
