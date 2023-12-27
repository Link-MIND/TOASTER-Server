package com.app.linkmind.service.auth.apple;

import java.security.PublicKey;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.app.linkmind.service.auth.apple.response.ApplePublicKeys;
import com.app.linkmind.service.auth.apple.verify.AppleJwtParser;
import com.app.linkmind.service.auth.apple.verify.PublicKeyGenerator;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppleSignInService {
	private static final String APPLE_URI = "https://appleid.apple.com/auth";
	private static final RestClient restClient = RestClient.create(APPLE_URI);
	private static AppleJwtParser appleJwtParser;
	private static PublicKeyGenerator publicKeyGenerator;

	public String getAppleId(String identityToken) {
		Map<String, String> headers = appleJwtParser.parseHeaders(identityToken);

		ResponseEntity<ApplePublicKeys> result = restClient.get()
			.uri("/keys")
			.retrieve()
			.toEntity(ApplePublicKeys.class);

		PublicKey publicKey = publicKeyGenerator.generatePublicKey(headers, result.getBody());

		Claims claims = appleJwtParser.parsePublicKeyAndGetClaims(identityToken, publicKey);
		return claims.getSubject();
	}
}
