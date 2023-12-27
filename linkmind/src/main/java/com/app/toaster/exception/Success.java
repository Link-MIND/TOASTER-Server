package com.app.toaster.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Success {

	/**
	 * 201 CREATED
	 */
	CREATE_RESERVATION_SUCCESS(HttpStatus.CREATED, "예약 사이트로 이동 중 입니다."),


	/**
	 * 200 OK
	 */
	GET_AIR_MINPRICE_SUCESS(HttpStatus.OK, "항공사별 최저가격 조회 성공"),
	GET_RESERVATION_SUCCESS(HttpStatus.OK, "항공권 페이지 조회 성공"),
	GET_MAIN_SUCCESS(HttpStatus.OK, "메인 페이지 조회 성공"),
	GET_TICKET_SUCCESS(HttpStatus.OK, "티켓 선택 페이지 조회 성공"),

	/**
	 * 204 NO_CONTENT
	 */

	;

	private final HttpStatus httpStatus;
	private final String message;

	public int getHttpStatusCode(){
		return httpStatus.value();
	}

}
