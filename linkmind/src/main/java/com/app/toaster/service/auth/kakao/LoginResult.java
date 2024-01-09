package com.app.toaster.service.auth.kakao;

public record LoginResult(String id, String profile) {
	public static LoginResult of(String id, String profile){
		return new LoginResult(id,profile);
	}
}
