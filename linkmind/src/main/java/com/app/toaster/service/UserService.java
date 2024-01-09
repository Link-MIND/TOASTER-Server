package com.app.toaster.service;

import org.springframework.beans.factory.annotation.Value;
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
import com.app.toaster.service.toast.ToastService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final ToastRepository toastRepository;
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


}
