package com.app.toaster.toast;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.app.toaster.domain.Toast;
import com.app.toaster.domain.User;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.infrastructure.ToastRepository;
import com.app.toaster.infrastructure.UserRepository;

@DataJpaTest //@Transactional 어노테이션을 포함하고 있다. 그래서 테스트가 완료되면 자동으로 롤백.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
public class ToastRepositoryTest {

	@Autowired
	ToastRepository toastRepository;

	@Autowired
	UserRepository userRepository;

	@Test
	public void 카테고리가null일때_저장_테스트(){
		//given
		String url ="https://www.naver.com";

		User presentUser =  userRepository.findByUserId(125L).orElseThrow(
			()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
		);

		//when
		Toast toast = Toast.builder()
				.user(presentUser)
				.linkUrl(url)
				.title("하이")
				.thumbnailUrl("기본이미지url")
				.build();
		// 카테고리 null로 테스트
		toast.updateCategory(null);
		toastRepository.save(toast);

		//then
		System.out.println(toast.getId());
		Assertions.assertEquals(toast.getTitle(),"하이");
		Assertions.assertEquals(toast.getThumbnailUrl(),"기본이미지url");
	}
}
