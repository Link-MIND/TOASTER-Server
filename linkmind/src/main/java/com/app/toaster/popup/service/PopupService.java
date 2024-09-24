package com.app.toaster.popup.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAmount;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.BadRequestException;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.popup.controller.request.PopUpRequestDto;
import com.app.toaster.popup.controller.response.InvisibleResponseDto;
import com.app.toaster.popup.controller.response.PopupResponseDto;
import com.app.toaster.popup.entity.Popup;
import com.app.toaster.popup.entity.PopupInvisibleManager;
import com.app.toaster.popup.infrastructure.PopupManagerRepository;
import com.app.toaster.popup.infrastructure.PopupRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PopupService {
	private final PopupManagerRepository popupManagerRepository;
	private final PopupRepository popupRepository;

	public InvisibleResponseDto updatePopupInvisible(Long userId, PopUpRequestDto popUpRequestDto) {
		Popup popup = findActivePopup(popUpRequestDto.popupId());

		PopupInvisibleManager manager = findManagerWithUserIdAndPopup(userId, popup);
		LocalDate today = LocalDate.now();

		manager.updateInvisible(today.plusDays(popUpRequestDto.hideDate()));
		return InvisibleResponseDto.of(popup.getId(), manager.getHideDateUntil());
	}

	@Transactional(readOnly = true)
	public PopupResponseDto findPopupInformation(Long userId) {
		List<Long> invisibleManagerList = findInvisiblePopupIdList(userId);
		return PopupResponseDto.from(popupRepository.findAll().stream()
			.filter((popup) -> popup.isActivePopup(LocalDate.now()))
			.filter(popup -> !invisibleManagerList.contains(popup.getId()))
			.toList());
	}

	private Popup findActivePopup(Long id) {
		LocalDate today = LocalDate.now();
		Popup popup = popupRepository.findById(id)
			.orElseThrow(()-> new NotFoundException(Error.NOT_FOUND_POPUP_EXCEPTION, Error.NOT_FOUND_POPUP_EXCEPTION.getMessage()));
		if (!popup.isActivePopup(today)){
			throw new BadRequestException(Error.BAD_REQUEST_ID, Error.BAD_REQUEST_ID.getMessage());
		}
		return popup;
	}

	private PopupInvisibleManager findManagerWithUserIdAndPopup(Long userId, Popup popup) {
		return popupManagerRepository.findByUserIdAndPopup(userId, popup)
			.orElseGet(() ->
				popupManagerRepository.save(PopupInvisibleManager.builder()
					.userId(userId)
					.popup(popup)
					.build())
			);
	}

	private List<Long> findInvisiblePopupIdList(Long userId) {
		return popupManagerRepository.findByUserId(userId).stream()
			.filter(
				(manager) -> manager.getHideDateUntil().isAfter(LocalDate.now()) //오늘 날짜는 보여야지
			)
			.map(PopupInvisibleManager::getPopup)
			.map(Popup::getId)
			.toList();
	}
}
