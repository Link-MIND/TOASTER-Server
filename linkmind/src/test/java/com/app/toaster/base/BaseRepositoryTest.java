package com.app.toaster.base;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.app.toaster.config.TestJpaQueryFactoryConfig;

// @Import(TestJpaQueryFactoryConfig.class)
@DataJpaTest
public abstract class BaseRepositoryTest {
}
