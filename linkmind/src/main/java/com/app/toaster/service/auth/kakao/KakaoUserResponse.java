package com.app.toaster.service.auth.kakao;

public record KakaoUserResponse(KakaoAccount kakaoAccount) {
	public static KakaoUserResponse of(KakaoAccount kakaoAccount){
		return new KakaoUserResponse(kakaoAccount);
	}
}
