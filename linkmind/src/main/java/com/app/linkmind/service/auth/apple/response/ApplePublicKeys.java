package com.app.linkmind.service.auth.apple.response;

import java.util.List;

import com.app.linkmind.exception.Error;
import com.app.linkmind.exception.model.CustomException;

public record ApplePublicKeys(List<ApplePublicKey> keys) {

	public ApplePublicKey getMatchesKey(String alg, String kid) {
		return this.keys
			.stream()
			.filter(k -> k.alg().equals(alg) && k.kid().equals(kid))
			.findFirst()
			.orElseThrow(() -> new CustomException(Error.INVALID_APPLE_PUBLIC_KEY, Error.INVALID_APPLE_PUBLIC_KEY.getMessage()));
	}
}
