package com.app.toaster.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.app.toaster.config.UserId;
import com.app.toaster.config.UserIdResolver;
import com.app.toaster.config.jwt.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;


public class MockUserIdResolver extends UserIdResolver {

	public MockUserIdResolver(JwtService jwtService) {
		super(jwtService);
	}

	@Override
	public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		return 125L;
	}

}
