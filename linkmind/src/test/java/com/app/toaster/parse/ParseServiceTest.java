package com.app.toaster.parse;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import javax.net.ssl.SSLHandshakeException;
import org.assertj.core.api.Assertions;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.app.toaster.controller.response.parse.OgResponse;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.BadRequestException;
import com.app.toaster.exception.model.CustomException;

class ParseServiceTest extends ParsingServiceTestImpl {

	private static final String BASIC_THUMBNAIL = "기본썸네일 이미지 주소";

	// //Test를 실행하기 전마다 ParsingService에 해줄 일.
	// @BeforeEach
	// void setUp(){
	// }

	@Test
	@DisplayName("일반적인 Url 네이버 사용성공 테스트")
	void basicSitesAvailable() throws IOException {
		/*
		given
		 */
		String basicUrl = "https://www.naver.com";
		String resultOfTitle = getOg(basicUrl).titleAdvanced();
		String resultOfImage = getOg(basicUrl).imageAdvanced();
		System.out.println(resultOfTitle);
		System.out.println(resultOfImage);
		Assertions.assertThat(resultOfImage).isNotEmpty(); // Assuming a non-empty image URL
		Assertions.assertThat(resultOfTitle).isEqualTo("네이버");
	}

	//현재 로직을 오버라이드해서 가져온다.
	@Override
	public OgResponse getOg(String linkUrl) throws IOException {
		try {
			String title = getTitle(linkUrl);
			String image = getImage(linkUrl);
			return OgResponse.of(
				title == null || title.isBlank() ? "기본 토스트 제목" : title,
				image == null || image.isBlank() ? BASIC_THUMBNAIL : image
			);
		} catch (HttpStatusException | SSLHandshakeException e) {
			return OgResponse.of("15자 내로 제목을 지어주세요.", BASIC_THUMBNAIL);
		} catch (ConnectException e) {
			throw new BadRequestException(Error.BAD_REQUEST_URL, Error.BAD_REQUEST_URL.getMessage());
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

	private String getImage(String linkUrl){
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
