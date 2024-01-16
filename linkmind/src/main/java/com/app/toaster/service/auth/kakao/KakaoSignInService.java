package com.app.toaster.service.auth.kakao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.HttpBody;
import com.google.api.HttpBodyOrBuilder;
import com.google.gson.JsonArray;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoSignInService {
	@Value("${jwt.KAKAO_URL}")
	private String KAKAO_URL;

	public LoginResult getKaKaoId(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization","Bearer "+ accessToken);

		HttpEntity<JsonArray> httpEntity = new HttpEntity<>(headers);
		ResponseEntity<Object> responseData;
		responseData = restTemplate.postForEntity(KAKAO_URL,httpEntity,Object.class);
		ObjectMapper objectMapper = new ObjectMapper();
		HashMap profileResponse = (HashMap)objectMapper.convertValue( responseData.getBody(),Map.class).get("properties");
		System.out.println(objectMapper.convertValue(responseData.getBody(), Map.class));
		return LoginResult.of(objectMapper.convertValue(responseData.getBody(), Map.class).get("id").toString(), profileResponse==null?null:profileResponse.get("profile_image").toString(),
			profileResponse==null?null:profileResponse.get("nickname").toString()); //프로필 이미지 허용 x시 null로 넘기기.
	}

}
