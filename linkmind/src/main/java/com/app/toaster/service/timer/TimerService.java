package com.app.toaster.service.timer;

import com.app.toaster.controller.request.fcm.FCMPushRequestDto;
import com.app.toaster.controller.request.timer.CreateTimerRequestDto;
import com.app.toaster.controller.request.timer.UpdateTimerCommentDto;
import com.app.toaster.controller.request.timer.UpdateTimerDateTimeDto;
import com.app.toaster.controller.response.timer.CompletedTimerDto;
import com.app.toaster.controller.response.timer.GetTimerPageResponseDto;
import com.app.toaster.controller.response.timer.GetTimerResponseDto;
import com.app.toaster.controller.response.timer.WaitingTimerDto;
import com.app.toaster.domain.Category;
import com.app.toaster.domain.Reminder;
import com.app.toaster.domain.User;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.CustomException;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.exception.model.UnauthorizedException;
import com.app.toaster.infrastructure.CategoryRepository;
import com.app.toaster.infrastructure.TimerRepository;
import com.app.toaster.infrastructure.UserRepository;
import com.app.toaster.service.fcm.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimerService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TimerRepository timerRepository;


    private final Locale locale = Locale.KOREA;

    private final int TimeIntervalInHours = 60;

    @Transactional
    public void createTimer(Long userId, CreateTimerRequestDto createTimerRequestDto){
        User presentUser = findUser(userId);

        int timerNum = timerRepository.findAllByUser(presentUser).size();

        if(timerNum>=5){
            throw new CustomException(Error.UNPROCESSABLE_ENTITY_CREATE_TIMER_EXCEPTION, Error.UNPROCESSABLE_ENTITY_CREATE_TIMER_EXCEPTION.getMessage());
        }

        Category category = categoryRepository.findById(createTimerRequestDto.categoryId())
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_CATEGORY_EXCEPTION, Error.NOT_FOUND_CATEGORY_EXCEPTION.getMessage()));


        Reminder reminder = Reminder.builder()
                .user(presentUser)
                .category(category)
                .remindDates(createTimerRequestDto.remindDates())
                .remindTime(LocalTime.parse(createTimerRequestDto.remindTime()))
                .isAlarm(true)
                .comment(category.getTitle())
                .build();


        timerRepository.save(reminder);
    }

    public GetTimerResponseDto getTimer(Long userId, Long timerId){
        Reminder reminder = timerRepository.findById(timerId)
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_TIMER, Error.NOT_FOUND_TIMER.getMessage()));

        return GetTimerResponseDto.of(reminder);
    }

    @Transactional
    public void updateTimerDatetime(Long userId, Long timerId, UpdateTimerDateTimeDto updateTimerDateTimeDto){
        User presentUser = findUser(userId);

        Reminder reminder = timerRepository.findById(timerId)
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_TIMER, Error.NOT_FOUND_TIMER.getMessage()));

        if (!presentUser.equals(reminder.getUser())){
            throw new UnauthorizedException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }
        reminder.updateRemindDates(updateTimerDateTimeDto.remindDates());
        reminder.updateRemindTime(updateTimerDateTimeDto.remindTime());

    }

    @Transactional
    public void updateTimerComment(Long userId, Long timerId, UpdateTimerCommentDto updateTimerCommentDto){
        User presentUser = findUser(userId);

        Reminder reminder = timerRepository.findById(timerId)
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_TIMER, Error.NOT_FOUND_TIMER.getMessage()));

        if (!presentUser.equals(reminder.getUser())){
            throw new UnauthorizedException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }

        reminder.updateComment(updateTimerCommentDto.newComment());

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

    @Transactional
    public void changeAlarm(Long userId, Long timerId){
        User presentUser = findUser(userId);

        Reminder reminder = timerRepository.findById(timerId)
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_TIMER, Error.NOT_FOUND_TIMER.getMessage()));

        if (!presentUser.equals(reminder.getUser())){
            throw new UnauthorizedException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }

        reminder.changeAlarm();
    }

    public GetTimerPageResponseDto getTimerPage(Long userId){
        User presentUser = findUser(userId);
        ArrayList<Reminder> reminders = timerRepository.findAllByUser(presentUser);

        List<CompletedTimerDto> completedTimerList = reminders.stream()
                .filter(this::isCompletedTimer)
                .map(this::createCompletedTimerDto)
                .sorted(Comparator.comparing(CompletedTimerDto::remindTime))
                .collect(Collectors.toList());


        List<WaitingTimerDto> waitingTimerList = reminders.stream()
                .filter(reminder -> !isCompletedTimer(reminder))
                .map(this::createWaitingTimerDto)
                .sorted(
                        Comparator.comparing(WaitingTimerDto::isAlarm)
                                .thenComparing(WaitingTimerDto::updateAt).reversed()
                )
                .collect(Collectors.toList());


        return GetTimerPageResponseDto.builder()
                .completedTimerList(completedTimerList)
                .waitingTimerList(waitingTimerList)
                .build();
    }

    //해당 유저 탐색
    private User findUser(Long userId){
        return userRepository.findByUserId(userId).orElseThrow(
                ()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
        );
    }

    // 완료된 타이머이고 알람이 켜져있는지 식별
    private boolean isCompletedTimer(Reminder reminder){
        // 현재 시간
        LocalDateTime now = LocalDateTime.now();
        List<LocalDateTime> resultDateTimeList = new ArrayList<>();


        // 주어진 요일 인덱스에 대해 LocalDateTime 생성 및 리스트에 추가
        for (Integer dayOfWeekIndex : reminder.getRemindDates()) {
            DayOfWeek currentDayOfWeek = now.getDayOfWeek();

            LocalDateTime newDateTime = now.plusDays(dayOfWeekIndex - currentDayOfWeek.getValue());
            newDateTime = LocalDateTime.of(newDateTime.toLocalDate(), reminder.getRemindTime());

            resultDateTimeList.add(newDateTime);

            if (currentDayOfWeek.getValue() == 1 || currentDayOfWeek.getValue() == 7) {
                resultDateTimeList.add(currentDayOfWeek.getValue() == 1 ? newDateTime.minusWeeks(1) : newDateTime.plusWeeks(1));
            }
        }

        for(LocalDateTime remind : resultDateTimeList){
            Duration duration = Duration.between(now, remind);
            if (Math.abs(duration.toMinutes()) <= TimeIntervalInHours) {
                return true;
            }
        }
        return false;
    }

    // 완료된 타이머 날짜,시간 포맷
    private CompletedTimerDto createCompletedTimerDto(Reminder reminder) {
        String time = reminder.getRemindTime().format(DateTimeFormatter.ofPattern("a hh:mm",locale));
        String date = getRemindDate(reminder);
        return CompletedTimerDto.of(reminder, time, date);
    }

    // 대기중인 타이머 날짜,시간 포맷
    private WaitingTimerDto createWaitingTimerDto(Reminder reminder) {
        String time = (reminder.getRemindTime().getMinute() == 0)
                ? reminder.getRemindTime().format(DateTimeFormatter.ofPattern("a h시",locale))
                : reminder.getRemindTime().format(DateTimeFormatter.ofPattern("a h시 mm분",locale));

        String dates = reminder.getRemindDates().stream()
                .map(this::mapIndexToDayString)
                .collect(Collectors.joining(", "));

        return WaitingTimerDto.of(reminder, time, dates);
    }

    // 인덱스로 요일 알아내기
    private String mapIndexToDayString(int index) {
        DayOfWeek dayOfWeek = DayOfWeek.of(index);
        String dayName = dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL,locale);

        return dayName.substring(0, 1);
    }

    private String getRemindDate(Reminder reminder){
        LocalDateTime remindDate = LocalDateTime.now();
        LocalTime now = LocalTime.now();

        // 비교할 시간 범위를 설정합니다.
        LocalTime startTime = LocalTime.of(23, 0);   // 11시
        LocalTime endTime = LocalTime.of(1, 0);      // 1시

        // 현재 시간이 11시 이후 또는 1시 이전인지 확인합니다.
        if (reminder.getRemindTime().isAfter(startTime) && now.isBefore(startTime)) {
            if(remindDate.getDayOfWeek().getValue() == 1 )
                remindDate = remindDate.plusDays(6);
            else
                remindDate = remindDate.minusDays(1);
        }
        if (reminder.getRemindTime().isBefore(endTime) && now.isAfter(endTime)) {
            if(remindDate.getDayOfWeek().getValue() == 7 )
                remindDate = remindDate.minusDays(6);
            else
                remindDate = remindDate.plusDays(1);
        }

        return remindDate.format(DateTimeFormatter.ofPattern("E요일",locale));
    }

}
