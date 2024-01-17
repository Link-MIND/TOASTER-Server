package com.app.toaster.service.auth.kakao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.ForbiddenException;
import com.app.toaster.exception.model.UnprocessableEntityException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoSignInService {
	@Value("${jwt.KAKAO_URL}")
	private String KAKAO_URL;

	@Value("${jwt.KAKAO_WITHDRAW}")
	private String KAKAO_WITHDRAW;

	public LoginResult getKaKaoId(String accessToken) {
		ResponseEntity<Object> responseData = requestKakaoServer(accessToken, Strategy.LOGIN);
		ObjectMapper objectMapper = new ObjectMapper();
		HashMap profileResponse = (HashMap)objectMapper.convertValue( responseData.getBody(),Map.class).get("properties");
		return LoginResult.of(objectMapper.convertValue(responseData.getBody(), Map.class).get("id").toString(), profileResponse==null?null:profileResponse.get("profile_image").toString(),
			profileResponse==null?null:profileResponse.get("nickname").toString()); //프로필 이미지 허용 x시 null로 넘기기.
	}
	// 인가코드 나중에 서버에서 한번에 처리 하는 방식으로 변경. ux 이슈

	// public String withdrawKakao(String accessToken){
	// 	ResponseEntity<Object> responseData = requestKakaoServer(accessToken, Strategy.WITHDRAWAL);
	// 	ObjectMapper objectMapper = new ObjectMapper();
	// 	HashMap profileResponse = (HashMap)objectMapper.convertValue( responseData.getBody(),Map.class);
	// 	return profileResponse.get("id").toString();
	// }

	private ResponseEntity<Object> requestKakaoServer(String accessToken, Strategy strategy){
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization","Bearer "+ accessToken);

		HttpEntity<JsonArray> httpEntity = new HttpEntity<>(headers);
		ResponseEntity<Object> responseData;
		switch (strategy){
			case WITHDRAWAL -> {
				return restTemplate.postForEntity(KAKAO_WITHDRAW,httpEntity,Object.class);
			}
			case LOGIN -> {
				return restTemplate.postForEntity(KAKAO_URL, httpEntity, Object.class);
			}
		}
		throw new UnprocessableEntityException(Error.UNPROCESSABLE_KAKAO_SERVER_EXCEPTION, Error.UNPROCESSABLE_KAKAO_SERVER_EXCEPTION.getMessage());
	}

}
