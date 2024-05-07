package com.app.toaster.fixture;

import com.app.toaster.domain.Category;
import com.app.toaster.domain.SocialType;
import com.app.toaster.domain.Toast;
import com.app.toaster.domain.User;

public class Fixture {
	public static final User USER_1 = User.builder()
		.nickname("유저첫번째")
		.socialId("유저아이디1")
		.socialType(SocialType.KAKAO)
		.build();

	public static final Category CATEGORY_1 = Category.builder()
		.title("카테고리 1번째")
		.priority(3)
		.user(USER_1)
		.build();

	public static final Category CATEGORY_2 = Category.builder()
		.title("카테고리 2번째")
		.priority(3)
		.user(USER_1)
		.build();

	public static final Toast TOAST_1 = Toast.builder()
		.user(USER_1)
		.category(CATEGORY_1)
		.thumbnailUrl("dd")
		.title("toast1")
		.linkUrl("toast링크url")
		.build();
	public static final Toast TOAST_2 = Toast.builder()
		.user(USER_1)
		.category(CATEGORY_1)
		.thumbnailUrl("ddd")
		.title("toast2")
		.linkUrl("toast링크url2")
		.build();
	public static final Toast TOAST_3 = Toast.builder()
		.user(USER_1)
		.category(CATEGORY_1)
		.thumbnailUrl("dd")
		.title("toast3")
		.linkUrl("toast링크url3")
		.build();
}
