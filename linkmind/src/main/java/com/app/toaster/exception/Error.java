package com.app.toaster.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Error {

	/**
	 * 404 NOT FOUND
	 */
	DUMMY_NOT_FOUND(HttpStatus.NOT_FOUND, "더미에 데이터가 덜 들어간 것 같아요"),

	RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약정보를 찾지 못했어요"),
	/**
	 * 500 INTERNAL_SERVER_ERROR
	 */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러가 발생했습니다")
	;

	private final HttpStatus httpStatus;
	private final String message;

	public int getErrorCode() {
		return httpStatus.value();
	}
}
