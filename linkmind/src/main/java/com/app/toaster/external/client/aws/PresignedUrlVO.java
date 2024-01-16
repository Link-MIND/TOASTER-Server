package com.app.toaster.external.client.aws;

public record PresignedUrlVO(String fileName, String url) {
	public static PresignedUrlVO of(String fileName, String url){
		return new PresignedUrlVO(fileName, url);
	}
}
