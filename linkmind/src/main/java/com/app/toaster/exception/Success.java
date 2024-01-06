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
	CREATE_TOAST_SUCCESS(HttpStatus.CREATED, "토스트 저장이 완료 되었습니다."),
	CREATE_CATEGORY_SUCCESS(HttpStatus.CREATED, "새 카테고리 추가 성공"),
	CREATE_TIMER_SUCCESS(HttpStatus.CREATED, "새 타이머 생성 성공"),

	/**
	 * 200 OK
	 */
	GET_MAIN_SUCCESS(HttpStatus.OK, "메인 페이지 조회 성공"),
	GET_CATEORIES_SUCCESS(HttpStatus.OK, "전체 카테고리 조회 성공"),
	GET_CATEORY_SUCCESS(HttpStatus.OK, "세부 카테고리 조회 성공"),
	GET_TIMER_SUCCESS(HttpStatus.OK, "타이머 조회 성공"),

  	LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
	RE_ISSUE_TOKEN_SUCCESS(HttpStatus.OK, "토큰 재발급 성공"),
	SIGNOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),
	DELETE_USER_SUCCESS(HttpStatus.OK, "유저 삭제 성공"),
	DELETE_TOAST_SUCCESS(HttpStatus.OK, "토스트 삭제 성공"),
	DELETE_CATEGORY_SUCCESS(HttpStatus.OK, "카테고리 삭제 성공"),
	SEARCH_SUCCESS(HttpStatus.OK, "검색 성공"),


	UPDATE_ISREAD_SUCCESS(HttpStatus.OK, "열람여부 수정 완료"),
	UPDATE_CATEGORY_TITLE_SUCCESS(HttpStatus.OK, "카테고리 수정 완료"),
	UPDATE_TIMER_DATETIME_SUCCESS(HttpStatus.OK, "타이머 시간/날짜 수정 완료"),
	UPDATE_TIMER_COMMENT_SUCCESS(HttpStatus.OK, "타이머 코멘트 수정 완료"),


	/**
	 * 204 NO_CONTENT
	 */
	SEARCH_SUCCESS_BUT_IS_EMPTY(HttpStatus.NO_CONTENT, "검색에 성공했지만 조회된 내용이 없습니다.")

	;

	private final HttpStatus httpStatus;
	private final String message;

	public int getHttpStatusCode(){
		return httpStatus.value();
	}

}
