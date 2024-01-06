package com.app.toaster.service.Timer;

import com.app.toaster.controller.request.timer.CreateTimerRequestDto;
import com.app.toaster.controller.request.timer.UpdateCategoryDateTimeDto;
import com.app.toaster.controller.response.timer.GetTimerResponseDto;
import com.app.toaster.controller.response.toast.ToastDto;
import com.app.toaster.domain.Category;
import com.app.toaster.domain.Reminder;
import com.app.toaster.domain.User;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.exception.model.UnauthorizedException;
import com.app.toaster.infrastructure.CategoryRepository;
import com.app.toaster.infrastructure.TimerRepository;
import com.app.toaster.infrastructure.UserRepository;
import com.app.toaster.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TimerService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TimerRepository timerRepository;

    @Transactional
    public void createTimer(Long userId, CreateTimerRequestDto createTimerRequestDto){
        User presentUser = findUser(userId);

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

    public GetTimerResponseDto getTimer(Long userId, Long timerId){
        Reminder reminder = timerRepository.findById(timerId)
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_TIMER, Error.NOT_FOUND_TIMER.getMessage()));

        return GetTimerResponseDto.of(reminder);
    }

    @Transactional
    public void updateTimerDatetime(Long userId, Long timerId, UpdateCategoryDateTimeDto updateCategoryDateTimeDto){
        User presentUser = findUser(userId);

        Reminder reminder = timerRepository.findById(timerId)
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_TIMER, Error.NOT_FOUND_TIMER.getMessage()));

        if (!presentUser.equals(reminder.getUser())){
            throw new UnauthorizedException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }
        reminder.updateRemindDates(updateCategoryDateTimeDto.remindDate());
        reminder.updateRemindTime(updateCategoryDateTimeDto.remindTime());

    }

    @Transactional
    public void deleteTimer(Long userId, Long timerId){
        User presentUser = findUser(userId);

        Reminder reminder = timerRepository.findById(timerId)
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_TIMER, Error.NOT_FOUND_TIMER.getMessage()));

        if (!presentUser.equals(reminder.getUser())){
            throw new UnauthorizedException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }

        timerRepository.delete(reminder);
    }

    //해당 유저 탐색
    private User findUser(Long userId){
        return userRepository.findByUserId(userId).orElseThrow(
                ()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
        );
    }
}
