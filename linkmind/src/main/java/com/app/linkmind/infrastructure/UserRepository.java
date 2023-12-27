package com.app.linkmind.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.linkmind.domain.SocialType;
import com.app.linkmind.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Boolean existsBySocialIdAndSocialType(String socialId, SocialType socialType);

	Optional<User> findByUserId(Long userId);

	Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType);

	boolean existsByNickname(String s);

	Optional<User> findByRefreshToken(String refreshToken);

	Long deleteByUserId(Long userId);
}
