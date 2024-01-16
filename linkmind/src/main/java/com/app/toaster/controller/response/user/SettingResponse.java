package com.app.toaster.controller.response.user;

public record SettingResponse(String nickname, Boolean fcmIsAllowed) {
	public static SettingResponse of(String nickname, Boolean fcmIsAllowed){
		return new SettingResponse(nickname,fcmIsAllowed);
	}
}
