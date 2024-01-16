package com.app.toaster.service;

import com.app.toaster.controller.response.category.CategoryResponse;
import com.app.toaster.controller.response.main.MainPageResponseDto;
import com.app.toaster.domain.Category;
import com.app.toaster.infrastructure.CategoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.toaster.controller.response.user.MyPageResponse;
import com.app.toaster.controller.response.user.SettingResponse;
import com.app.toaster.domain.User;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.BadRequestException;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.infrastructure.ToastRepository;
import com.app.toaster.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final ToastRepository toastRepository;
	private final CategoryRepository categoryRepository;

	public MyPageResponse getMyPage(Long userId){
		User user = userRepository.findByUserId(userId)
			.orElseThrow(()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));
		return MyPageResponse.of(
			user.getNickname(),
			user.getProfile(),
			toastRepository.countAllByUser(user),
			toastRepository.countALLByUserAndIsReadTrue(user),
			toastRepository.countAllByUserAndIsReadFalse(user)
		);
	}
	//푸시알림 동의 여부 수정 api
	@Transactional
	public Boolean allowedPushNotification(Long userId, Boolean fcmIsAllowed) {
		User user = userRepository.findByUserId(userId)
			.orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION,
				Error.NOT_FOUND_USER_EXCEPTION.getMessage()));
		if (fcmIsAllowed == user.getFcmIsAllowed()) {   //같은 경우면 에러가 날 수 있으니 에러 띄움.
			throw new BadRequestException(Error.BAD_REQUEST_VALIDATION,
				Error.BAD_REQUEST_VALIDATION.getMessage());
		}
		user.updateFcmIsAllowed(fcmIsAllowed);
		return fcmIsAllowed;
	}

	public SettingResponse getSettings(Long userId){
		User user = userRepository.findByUserId(userId)
			.orElseThrow(()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));
		return SettingResponse.of(
			user.getNickname(),
			user.getFcmIsAllowed()
		);
	}

	public MainPageResponseDto getMainPage(Long userId){
		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));

        int allToastNum = toastRepository.getAllByUser(user).size();
        int readToastNum = toastRepository.getAllByUserAndIsReadIsTrue(user).size();

        MainPageResponseDto mainPageResponseDto = MainPageResponseDto.builder().nickname(user.getNickname())
                .allToastNum(allToastNum)
                .readToastNum(readToastNum)
                .mainCategoryListDto(getCategory(user).stream()
                .map(category -> CategoryResponse.builder()
                        .categoryId(category.getCategoryId())
                        .categoryTitle(category.getTitle())
                        .toastNum(toastRepository.getAllByCategory(category).size()).build()
                ).collect(Collectors.toList())).build();

        return mainPageResponseDto;

	}


	private ArrayList<Category> getCategory(User user){

		ArrayList<Category> categories = categoryRepository.findAllByUser(user);

		if(categories.size()<4)
			return categories;

		return categoryRepository.findTop3ByUserOrderByLatestReadTimeDesc(user);

	}

}
