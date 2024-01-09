package com.app.toaster.service.parse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// import com.app.toaster.external.client.aws.S3Service;

import com.app.toaster.controller.response.parse.OgResponse;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.CustomException;
import com.app.toaster.external.client.aws.AWSConfig;

import lombok.RequiredArgsConstructor;

@Service
public class ParsingService {
	private final String BASIC_THUMBNAIL;

	public ParsingService(@Value("${static-image.url}") final String basicThumbnail) {
		this.BASIC_THUMBNAIL = basicThumbnail;
	}

	public OgResponse getOg(String linkUrl) throws IOException {
		try {
			String title = getTitle(linkUrl);
			String image = getImage(linkUrl);
			return OgResponse.of(
				title == null || title.isBlank() ? "15자 내로 제목을 지어주세요." : title,
				image == null || image.isBlank() ? BASIC_THUMBNAIL : image
			);
		}catch (org.jsoup.HttpStatusException e){
			return OgResponse.of("15자 내로 제목을 지어주세요.", BASIC_THUMBNAIL);
		}
	}

	private String getTitle(String linkUrl) throws IOException {
		try {
			Document doc = Jsoup.connect(linkUrl).get();
			Elements ogTitleElements = doc.select("meta[property=og:title]");
			Elements titleElements = doc.select("head").select("title");
			if (ogTitleElements.isEmpty() && titleElements.isEmpty()) {
				return null;
			}
			return ogTitleElements.isEmpty()?titleElements.get(0).text(): ogTitleElements.get(0).attr("content");
		}catch (org.jsoup.HttpStatusException e){
			return null;
		}

	}

	private String getImage(String linkUrl) throws IOException {
		try {
			Document doc = Jsoup.connect(linkUrl).get();
			Elements iframes = doc.select("iframe");
			Elements ogBlogImage = new Elements();
			if (!iframes.isEmpty()){
				Document iframeDoc = Jsoup.parse(doc.select("iframe").get(0).html());
				ogBlogImage = iframeDoc.select("meta[property=og:image]");
			}
			Elements ogImageElements = doc.select("meta[property=og:image]");
			Elements ogImage = doc.select("img[property=src]");
			//짜증나게 iframe 안에 박아놓은 경우.
			return 	findImageAnywhere(ogImageElements, ogImage, ogBlogImage);
		}catch (MalformedURLException e){
			throw new CustomException(Error.MALFORMED_URL_EXEPTION,Error.MALFORMED_URL_EXEPTION.getMessage());
		}catch (org.jsoup.HttpStatusException e){
			return null;
		}
	}

	private String findImageAnywhere(Elements metaCase, Elements imgCase, Elements iframeCase){
		if (!metaCase.isEmpty()){
			return metaCase.get(0).attr("content");
		}
		else if(!imgCase.isEmpty()){
			return metaCase.get(0).text();
		}
		else if(!iframeCase.isEmpty()){
			return iframeCase.get(0).attr("content");
		}
		else{
			return null;
		}
	}

}
