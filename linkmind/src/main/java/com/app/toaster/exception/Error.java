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
	NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "찾을 수 없는 유저입니다."),
	NOT_FOUND_CATEGORY_EXCEPTION(HttpStatus.NOT_FOUND, "찾을 수 없는 카테고리 입니다."),
	NOT_FOUND_TOAST_EXCEPTION(HttpStatus.NOT_FOUND, "찾을 수 없는 토스트 입니다."),
	NOT_FOUND_IMAGE_EXCEPTION(HttpStatus.NOT_FOUND, "s3 서비스에서 이미지를 찾을 수 없습니다."),


	/**
	 * 400 BAD REQUEST EXCEPTION
	 */
	BAD_REQUEST_ISREAD(HttpStatus.BAD_REQUEST, "isRead 값이 잘못요청 되었습니다."),
	BAD_REQUEST_VALIDATION(HttpStatus.BAD_REQUEST, "유효한 값으로 요청을 다시 보내주세요."),
	BAD_REQUEST_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "파일형식이 잘못된 것 같습니다."),
	BAD_REQUEST_FILE_SIZE(HttpStatus.BAD_REQUEST, "파일크기가 잘못된 것 같습니다. 최대 5MB"),
	MALFORMED_URL_EXEPTION(HttpStatus.BAD_REQUEST, "url 링크가 잘못된 것 같습니다."),


	/**
	 * 401 UNAUTHORIZED EXCEPTION
	 */
	TOKEN_TIME_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
	INVALID_APPLE_PUBLIC_KEY(HttpStatus.UNAUTHORIZED, "유효하지않은 애플 퍼블릭 키 입니다."),
	EXPIRED_APPLE_IDENTITY_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 아이덴티티 토큰입니다."),
	INVALID_APPLE_IDENTITY_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 아이덴티티 토큰입니다."),
	INVALID_USER_ACCESS(HttpStatus.UNAUTHORIZED, "접근 권한이 없는 유저입니다."),

	UNPROCESSABLE_ENTITY_DELETE_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "서버에서 요청을 이해해 삭제하려는 도중 문제가 생겼습니다."),
	/**
	 * 500 INTERNAL_SERVER_ERROR
	 */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러가 발생했습니다"),
	INVALID_ENCRYPT_COMMUNICATION(HttpStatus.INTERNAL_SERVER_ERROR, "ios 통신 증명 과정 중 문제가 발생했습니다."),
	CREATE_PUBLIC_KEY_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "publickey 생성 과정 중 문제가 발생했습니다."),
	CREATE_TOAST_PROCCESS_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "토스트 저장 중 문제가 발생했습니다. 카테고리 또는 s3 관련 문제로 예상됩니다.")
	;

	private final HttpStatus httpStatus;
	private final String message;

	public int getErrorCode() {
		return httpStatus.value();
	}
}
