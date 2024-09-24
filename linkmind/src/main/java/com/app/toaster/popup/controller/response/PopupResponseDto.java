package com.app.toaster.popup.controller.response;

import java.util.List;

import com.app.toaster.popup.entity.Popup;

public record PopupResponseDto(List<Popup> popupList) {

	public static PopupResponseDto from(List<Popup> popupList){
		return new PopupResponseDto(popupList);
	}
}
