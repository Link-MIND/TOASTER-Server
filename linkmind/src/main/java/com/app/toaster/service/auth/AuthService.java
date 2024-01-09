package com.app.toaster.service.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.toaster.config.jwt.JwtService;
import com.app.toaster.controller.request.auth.SignInRequestDto;
import com.app.toaster.controller.response.auth.SignInResponseDto;
import com.app.toaster.controller.response.auth.TokenResponseDto;
import com.app.toaster.domain.SocialType;
import com.app.toaster.domain.User;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.BadRequestException;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.exception.model.UnprocessableEntityException;
import com.app.toaster.infrastructure.UserRepository;
import com.app.toaster.service.auth.apple.AppleSignInService;
import com.app.toaster.service.auth.kakao.KakaoSignInService;
import com.app.toaster.service.auth.kakao.LoginResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final AppleSignInService appleSignInService;
	private final KakaoSignInService kakaoSignInService;
	private final JwtService jwtService;

	private final UserRepository userRepository;


	private final Long TOKEN_EXPIRATION_TIME_ACCESS = 1 * 60 * 1000L;
	private final Long TOKEN_EXPIRATION_TIME_REFRESH = 3 * 60 * 1000L;
	@Value("${static-image.root}")
	private String BASIC_ROOT;

	@Value("${static-image.url}")
	private String BASIC_THUMBNAIL;


	@Transactional
	public SignInResponseDto signIn(String socialAccessToken, SignInRequestDto requestDto) {
		SocialType socialType = SocialType.valueOf(requestDto.socialType());
		LoginResult loginResult = login(socialType, socialAccessToken);
		String socialId = loginResult.id();
		String profileImage = loginResult.profile();
		System.out.println(profileImage);
		Boolean isRegistered = userRepository.existsBySocialIdAndSocialType(socialId, socialType);

		if (!isRegistered) {
			User newUser = User.builder()
				.nickname("토스터"+socialId)
				.socialId(socialId)
				.socialType(socialType).build();
			newUser.updateFcmIsAllowed(true); //신규 유저면 true박고
			userRepository.save(newUser);
		}

		User user = userRepository.findBySocialIdAndSocialType(socialId, socialType)
			.orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));
		System.out.println(user);
		System.out.println(user.getUserId());

		// jwt 발급 (액세스 토큰, 리프레쉬 토큰)
		String accessToken = jwtService.issuedToken(String.valueOf(user.getUserId()), TOKEN_EXPIRATION_TIME_ACCESS);
		String refreshToken = jwtService.issuedToken(String.valueOf(user.getUserId()), TOKEN_EXPIRATION_TIME_REFRESH);
		String fcmToken = requestDto.fcmToken();

		user.updateRefreshToken(refreshToken);
		user.updateFcmToken(fcmToken);
		user.updateProfile(profileImage == null ? BASIC_ROOT+BASIC_THUMBNAIL : profileImage);


		return SignInResponseDto.of(user.getUserId(), accessToken, refreshToken, fcmToken, isRegistered,user.getFcmIsAllowed(),
			user.getProfile());
	}

	@Transactional
	public TokenResponseDto issueToken(String refreshToken) {
		jwtService.verifyToken(refreshToken);

		User user = userRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));

		// jwt 발급 (액세스 토큰, 리프레쉬 토큰)
		String newAccessToken = jwtService.issuedToken(String.valueOf(user.getUserId()), TOKEN_EXPIRATION_TIME_ACCESS);
		String newRefreshToken = jwtService.issuedToken(String.valueOf(user.getUserId()), TOKEN_EXPIRATION_TIME_REFRESH);

		user.updateRefreshToken(newRefreshToken);

		return TokenResponseDto.of(newAccessToken, newRefreshToken);
	}

	@Transactional
	public void signOut(Long userId) {
		User user = userRepository.findByUserId(userId)
			.orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));
		user.updateRefreshToken(null);
		user.updateFcmToken(null);
	}

	private LoginResult login(SocialType socialType, String socialAccessToken) {
		if (socialType.toString() == "APPLE") {
			return appleSignInService.getAppleId(socialAccessToken);
		}
		else if (socialType.toString() == "KAKAO") {

			return kakaoSignInService.getKaKaoId(socialAccessToken);
		}
		else{
			return LoginResult.of("test", "뭔가 로직에 문제가 있음.");
		}
	}

	@Transactional
	public void withdraw(Long userId){
		User user = userRepository.findByUserId(userId).orElse(null);
		System.out.println(userId);
		if (user == null) {
			throw new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage());
		}

		Long res = userRepository.deleteByUserId(userId); //res가 삭제된 컬럼의 개수 즉, 1이 아니면 뭔가 알 수 없는 에러.
		System.out.println(res + "개의 컬럼이 삭제되었습니다.");
		if (res!=1){
			throw new UnprocessableEntityException(Error.UNPROCESSABLE_ENTITY_DELETE_EXCEPTION, Error.UNPROCESSABLE_ENTITY_DELETE_EXCEPTION.getMessage());
		}
	}
}
