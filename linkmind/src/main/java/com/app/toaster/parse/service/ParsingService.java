package com.app.toaster.parse.service;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;

import javax.net.ssl.SSLHandshakeException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// import com.app.toaster.external.client.aws.S3Service;

import com.app.toaster.parse.controller.response.OgResponse;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.BadRequestException;
import com.app.toaster.exception.model.CustomException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ParsingService {
	private final String BASIC_THUMBNAIL;

	public ParsingService(@Value("${static-image.url}") final String basicThumbnail) {
		this.BASIC_THUMBNAIL = basicThumbnail;
	}

	public OgResponse getOg(String linkUrl) {
		String title = getTitle(linkUrl);
		log.info(title);
		String image = getImage(linkUrl);
		log.info(image);
		return OgResponse.of(
			title == null || title.isBlank() ? "기본 토스트 제목" : title,
			image == null || image.isBlank() ? BASIC_THUMBNAIL : image
		);
	}
	// public String getOg(String linkUrl) throws IOException {
	// 	String image = getImage(linkUrl);
	// 	return image == null || image.isBlank() ? BASIC_THUMBNAIL : image;
	// }

	private String getTitle(String linkUrl) {
		try {
			Document doc = Jsoup.connect(linkUrl)
				.followRedirects(true)  // 리다이렉션 자동 따라가기
				.maxBodySize(1024*1024)  // 페이지 크기 제한 없음
				.timeout(10000)
				.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
				.get();
			Elements ogTitleElements = doc.select("meta[property=og:title]");
			Elements titleElements = doc.select("head").select("title");
			if (ogTitleElements.isEmpty() && titleElements.isEmpty()) {
				log.info("[NOT FOUND] og 데이터, html header 뜯었는데 결과 없음.");
				return "15자 내로 제목을 지어주세요.";
			}
			return ogTitleElements.isEmpty()?titleElements.get(0).text(): ogTitleElements.get(0).attr("content");
		}catch (org.jsoup.HttpStatusException e){
			log.info("[ERROR] title 파싱 중 http status 에러 발생");
			return "15자 내로 제목을 지어주세요.";
		} catch (SSLHandshakeException e){
			log.info("[ERROR] 너무 오래된 사이트라 handshake 규칙이 맞지 않습니다.");
			return "15자 내로 제목을 지어주세요.";
		} catch (IOException e){
			log.info("[ERROR] title 파싱 중 에러 발생");
			return "15자 내로 제목을 지어주세요.";
		}
	}

	private String getImage(String linkUrl){
		try {
			Document doc = Jsoup.connect(linkUrl)
				.followRedirects(true)  // 리다이렉션 자동 따라가기
				.maxBodySize(1024*1024)  // 페이지 크기 제한 없음
				.timeout(10000)
				.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
				.get();

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
		}catch (org.jsoup.HttpStatusException e) {
			return null;
		}catch (SSLHandshakeException e){
			log.info("[ERROR] 너무 오래된 사이트라 handshake 규칙이 맞지 않습니다.");
			return null;
		}catch (IOException e){
			throw new CustomException(Error.NOT_FOUND_IMAGE_EXCEPTION, Error.NOT_FOUND_IMAGE_EXCEPTION.getMessage());
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
