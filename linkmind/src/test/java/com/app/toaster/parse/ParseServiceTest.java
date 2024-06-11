package com.app.toaster.parse;
import static org.springframework.web.util.UriUtils.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		/*
		when
		 */
		String resultOfTitle = getOg(basicUrl).titleAdvanced();
		String resultOfImage = getOg(basicUrl).imageAdvanced();

		/*
		then
		 */
		System.out.println(resultOfTitle);
		System.out.println(resultOfImage);
		Assertions.assertThat(resultOfImage).isNotEmpty(); // Assuming a non-empty image URL
		Assertions.assertThat(resultOfTitle).isEqualTo("네이버");
	}

	@Test
	@DisplayName("브런치 Url 성공 테스트")
	void brunchSitesAvailable() throws IOException {
		/*
		given
		 */
		String basicUrl = "https://brunch.co.kr/@cat4348/74";
		/*
		when
		 */
		String resultOfTitle = getOg(basicUrl).titleAdvanced();
		String resultOfImage = getOg(basicUrl).imageAdvanced();

		/*
		then
		 */
		System.out.println(resultOfTitle);
		System.out.println(resultOfImage);
		Assertions.assertThat(resultOfImage).isNotEmpty(); // Assuming a non-empty image URL
		Assertions.assertThat(resultOfTitle).isEqualTo("오프라인 아이디어 수집, UX라이팅 등");
	}

	@Test
	@DisplayName("티스토리 Url 성공 테스트")
	void tistorySitesAvailable() throws IOException {
		/*
		given
		 */
		String basicUrl = "https://rorobong.tistory.com/m/125";

		/*
		when
		 */
		String resultOfTitle = getOg(basicUrl).titleAdvanced();
		String resultOfImage = getOg(basicUrl).imageAdvanced();

		/*
		then
		 */
		System.out.println(resultOfTitle);
		System.out.println(resultOfImage);
		Assertions.assertThat(resultOfImage).isNotEmpty(); // Assuming a non-empty image URL
		Assertions.assertThat(resultOfTitle).isEqualTo("[JAVA] 자바 제어자 (접근 지정자, static 제어자)");
	}

	@Test
	@DisplayName("깃허브 Url 성공 테스트")
	void gitHubSitesAvailable() throws IOException {
		/*
		given
		 */
		String basicUrl = "https://github.com/team-winey/Winey-Server";

		/*
		when
		 */
		String resultOfTitle = getOg(basicUrl).titleAdvanced();
		String resultOfImage = getOg(basicUrl).imageAdvanced();

		/*
		then
		 */
		System.out.println(resultOfTitle);
		System.out.println(resultOfImage);
		Assertions.assertThat(resultOfImage).isNotEmpty(); // Assuming a non-empty image URL
		Assertions.assertThat(resultOfTitle).isEqualTo("GitHub - team-winey/Winey-Server: \uD83D\uDCB8 아니, 어제만 해도 왕족이었던 내가 평민이라고?");
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
			System.out.println(titleElements);
			System.out.println(ogTitleElements);
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
			//인스타의 경우
			if (!iframes.isEmpty()){
				Document iframeDoc = Jsoup.parse(doc.select("iframe").get(0).html());
				// System.out.println("durl");
				ogBlogImage = iframeDoc.select("meta[property=og:image]");
			}
			Elements ogImageElements = doc.select("meta[property=og:image]");
			Elements ogImage = doc.select("img[property=src]");
			// System.out.println("1번"+ogImageElements); // tistory,naver
			// System.out.println("2번"+ogImage);	//brunch
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
		if (!metaCase.isEmpty()) {    //naver,brunch
			System.out.println("metaCase");
			String excludeSlug = metaCase.get(0).attr("content");
			String responseUrl = extractSlugUrl(excludeSlug);
			return responseUrl==null ? excludeSlug : responseUrl;
		} else if (!imgCase.isEmpty()) {
			System.out.println("imgCase");
			return metaCase.get(0).text();
		} else if (!iframeCase.isEmpty()) {
			System.out.println("iframeCase");
			return iframeCase.get(0).attr("content");
		} else {
			return null;
		}
	}

	private String extractSlugUrl(String slug) {
		// 정규표현식 패턴 설정
		Pattern pattern = Pattern.compile("(?<=fname=)([^&]+(?:%[0-9a-fA-F]{2})*)"); //fname=이라는게 앞에 나오면 그때부터 패턴 시작. &는 파라미터 구별하니 제외.
		// "/"같은거 16진수로 나오는 패턴이 %XX형태로 나오니 %이후에 16진수로 올수있는거 2자리가 *로 반복해서 올 수 있다를 명시.
		Matcher matcher = pattern.matcher(slug);

		// 매칭된 URL 추출
		if (matcher.find()) {
			// URL 디코딩 수행
			String url = matcher.group();
			url = decode(url, StandardCharsets.UTF_8);
			return url;
		}

			return null;
	}
}



