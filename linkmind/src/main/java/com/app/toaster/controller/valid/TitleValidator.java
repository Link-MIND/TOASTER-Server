package com.app.toaster.controller.valid;


import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.CustomException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TitleValidator implements ConstraintValidator<TitleValid, String> {

	public String pattern;
	@Override
	public void initialize(TitleValid constraintAnnotation) {
		this.pattern = constraintAnnotation.pattern();
	}

	@Override
	public boolean isValid(String title, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();

		if (title == null) {
			context.buildConstraintViolationWithTemplate("제목에 null값이 들어옵니다.") //defaultMessage 생성
				.addConstraintViolation();
			return false;
		}

		// 커스텀 예외를 던집니다.
		// null, 공백으로만 이뤄지는 경우, 빈 값인 경우 ''
		if (title.isBlank()) {
			context.buildConstraintViolationWithTemplate("제목이 공백으로만 차있습니다.") //defaultMessage 생성
				.addConstraintViolation();
			return false;
		}
		// 제목이 비어있는 경우
		if (title.isEmpty()) {
			context.buildConstraintViolationWithTemplate("제목이 비어있습니다. ")
				.addConstraintViolation();
			return false;
		}

		// 첫 글자가 공백인 경우
		return !(title.charAt(0) == ' ');
	}
}
