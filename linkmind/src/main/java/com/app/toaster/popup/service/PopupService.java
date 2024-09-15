package com.app.toaster.popup.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.toaster.exception.Error;
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

		PopupInvisibleManager manager = findManagerWithUserIdAndPopup(userId, popup, popUpRequestDto.invisible());

		manager.updateInvisible(popUpRequestDto.invisible()); // 어차피 더티체킹시에 똑같으면 쿼리 안나가겠지?
		return InvisibleResponseDto.of(popup.getId(), manager.isWantToInvisible());
	}

	@Transactional(readOnly = true)
	public PopupResponseDto findPopupInformation(Long userId) {
		List<Long> invisibleManagerList = findInvisiblePopupIdList(userId);
		return PopupResponseDto.from(popupRepository.findAll().stream()
			.filter(Popup::isActive)
			.filter(popup -> !invisibleManagerList.contains(popup.getId()))
			.toList());
	}

	private Popup findActivePopup(Long id) {
		return popupRepository.findByIdAndActive(id, true).orElseThrow(
			() -> new NotFoundException(Error.NOT_FOUND_POPUP_EXCEPTION, Error.NOT_FOUND_POPUP_EXCEPTION.getMessage()));
	}

	private PopupInvisibleManager findManagerWithUserIdAndPopup(Long userId, Popup popup, boolean invisible) {
		return popupManagerRepository.findByUserIdAndPopup(userId, popup)
			.orElseGet(() ->
				popupManagerRepository.save(PopupInvisibleManager.builder()
					.userId(userId)
					.popup(popup)
					.wantToInvisible(invisible)
					.build())
			);
	}

	private List<Long> findInvisiblePopupIdList(Long userId) {
		return popupManagerRepository.findByUserId(userId).stream()
			.filter(PopupInvisibleManager::isWantToInvisible)
			.map(PopupInvisibleManager::getPopup)
			.map(Popup::getId)
			.toList();
	}
}
