package com.app.toaster.popup.controller.response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;

public record InvisibleResponseDto(Long popupId, String hideUntil) {
	public static InvisibleResponseDto of(Long popupId, LocalDate hideUntil){
		return new InvisibleResponseDto(popupId, hideUntil.format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));
	}
}
