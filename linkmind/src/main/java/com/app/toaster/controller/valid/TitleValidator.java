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
		System.out.println("여기는 오는건가");
		context.disableDefaultConstraintViolation();
		// 커스텀 예외를 던집니다.
		// null, 공백으로만 이뤄지는 경우, 빈 값인 경우 ''
		if (title.isBlank()) {
			context.buildConstraintViolationWithTemplate("제목이 공백으로만 차있습니다.")
				.addConstraintViolation();
			return false;
		}
		// 길이가 1보다 작거나 10보다 큰 경우
		if (title.isEmpty()) {
			context.buildConstraintViolationWithTemplate("제목이 비어있습니다. ")
				.addConstraintViolation();
			return false;
		}

		// 첫 글자가 공백인 경우
		return !(title.charAt(0) == ' ');
	}
}
