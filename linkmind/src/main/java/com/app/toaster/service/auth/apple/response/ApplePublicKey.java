package com.app.toaster.service.auth.apple.response;

public record ApplePublicKey(
	String kty,
	String kid,
	String use,
	String alg,
	String n,
	String e
) {
}
