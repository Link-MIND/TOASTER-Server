package com.app.toaster.fixture;

import com.app.toaster.domain.Category;
import com.app.toaster.domain.SocialType;
import com.app.toaster.domain.Toast;
import com.app.toaster.domain.User;

public class Fixture {

	public static final User USER_1 = User.builder()
		.nickname("hi")
		.socialId("aaa")
		.socialType(SocialType.KAKAO)
		.build();

	public static final Category CATEGORY_1 =Category.builder()
		.title("hi")
		.priority(3)
		.user(USER_1)
		.build();

	public static final Category CATEGORY_2 = Category.builder()
		.title("hii")
		.priority(3)
		.user(USER_1)
		.build();

	public static final Toast TOAST_1 = Toast.builder()
		.user(USER_1)
		.category(CATEGORY_1)
		.thumbnailUrl("dd")
		.title("dd")
		.linkUrl("dfdsfa")
		.build();
	public static final Toast TOAST_2 = Toast.builder()
		.user(USER_1)
		.category(CATEGORY_1)
		.thumbnailUrl("dd")
		.title("dd")
		.linkUrl("dfdsfa")
		.build();
	public static final Toast TOAST_3 = Toast.builder()
		.user(USER_1)
		.category(CATEGORY_1)
		.thumbnailUrl("dd")
		.title("dd")
		.linkUrl("dfdsfa")
		.build();


}
