package com.app.toaster.controller.response.user;

public record MyPageResponse(String nickname, String profile, Long allReadToast, Long thisWeekendRead, Long thisWeekendSaved ) {
	public static MyPageResponse of(String nickname, String profile, Long allReadToast, Long thisWeekendRead, Long thisWeekendSaved ){
		return new MyPageResponse(nickname, profile, allReadToast, thisWeekendRead, thisWeekendSaved);
	}
}
