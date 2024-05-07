package com.app.toaster.infrastructure.querydsl;

import static com.app.toaster.domain.QToast.*;
import static com.app.toaster.fixture.Fixture.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.logging.Logger;

import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.toaster.config.JpaQueryFactoryConfig;
import com.app.toaster.domain.Category;
import com.app.toaster.domain.QToast;
import com.app.toaster.domain.SocialType;
import com.app.toaster.domain.Toast;
import com.app.toaster.domain.User;
import com.app.toaster.fixture.Fixture;
import com.app.toaster.infrastructure.CategoryRepository;
import com.app.toaster.infrastructure.ToastRepository;
import com.app.toaster.infrastructure.UserRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = true)
@Import(JpaQueryFactoryConfig.class)
class CustomToastRepositoryTest {

	CustomToastRepository customToastRepository;
	JPAQueryFactory jpaQueryFactory;

	@Autowired
	ToastRepository toastRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	EntityManager em;

	@BeforeEach
	void setup() {
		jpaQueryFactory = new JPAQueryFactory(em);
		customToastRepository = new CustomToastRepository(jpaQueryFactory);

		userRepository.save(USER_1);
		categoryRepository.save(CATEGORY_1);
		categoryRepository.save(CATEGORY_2);
		toastRepository.save(TOAST_1);
		toastRepository.save(TOAST_2);
		toastRepository.save(TOAST_3);
	}
	@AfterEach
	void tearDown() {
		// 테스트 후 정리 작업 수행
		em.clear();
		userRepository.deleteAll();
		categoryRepository.deleteAll();
		toastRepository.deleteAll();
	}


	@Nested
	@DisplayName("토스트 jpa repository test")
	class 토스트_JpaRepository_Test {

		@Test
		@DisplayName("토스트의 getAllByCategory test")
		@Transactional
		void test_ToastJpaRepository_조회() {
			List<Toast> toast = toastRepository.getAllByCategory(CATEGORY_1);
			Assertions.assertThat(toast.get(0).getId()).isEqualTo(Fixture.TOAST_1.getId());
			Assertions.assertThat(toast.get(1).getId()).isEqualTo(Fixture.TOAST_2.getId());
			Assertions.assertThat(toast.get(2).getId()).isEqualTo(Fixture.TOAST_3.getId());
		}
	}

	@Nested
	@DisplayName("토스트 QueryDSL test")
	class 토스트_QueryDSL_Test {

		@Test
		@DisplayName("토스트의 queryDSL getAllByCategory test")
		void ToastQueryRepository_getAllByCategory_테스트() {
			QToast qToast = new QToast("toast");

			List<Toast> toasts = customToastRepository.getAllByCategory(CATEGORY_1);

			Assertions.assertThat(toasts.get(0).getId()).isEqualTo(Fixture.TOAST_1.getId());
			Assertions.assertThat(toasts.get(1).getId()).isEqualTo(Fixture.TOAST_2.getId());
			Assertions.assertThat(toasts.get(2).getId()).isEqualTo(Fixture.TOAST_3.getId());
		}

		@Test
		@DisplayName("categoryid 결과를 찾지 못했을 때 test")
		void ToastQueryRepository_Category_id_2_테스트() {
			QToast qtoast = new QToast("toast");
			List<Toast> toasts = jpaQueryFactory.select(qtoast)
				.from(qtoast)
				.where(eqCategoryId(CATEGORY_2.getCategoryId()))
				.fetch();
			assertNotNull(toasts, "조회 데이터가 없습니다.");
			// Assertions.assertThat(toasts.get(0).getId()).isEqualTo(Fixture.TOAST_2.getId());
			// Assertions.assertThat(toasts.get(0).getId()).isEqualTo(Fixture.TOAST_3.getId());
		}

	}
	private BooleanExpression eqCategoryId(Long id){
		if (id == null){
			return null;
		}
		return toast.category.categoryId.eq(id);
	}

}