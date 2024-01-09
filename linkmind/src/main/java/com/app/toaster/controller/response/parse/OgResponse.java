package com.app.toaster.controller.response.parse;

public record OgResponse(String titleAdvanced, String imageAdvanced) {
	public static OgResponse of(String titleAdvanced, String imageAdvanced){
		return new OgResponse(titleAdvanced, imageAdvanced);
	}
}
