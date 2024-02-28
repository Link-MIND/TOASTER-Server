package com.app.toaster.base;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.app.toaster.config.TestJpaQueryFactoryConfig;

// @Import(TestJpaQueryFactoryConfig.class) -> 쿼리dsl 사용 시 사용할 것.
@DataJpaTest //@Transactional 어노테이션을 포함하고 있다. 그래서 테스트가 완료되면 자동으로 롤백.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
public abstract class BaseRepositoryTest {
}
