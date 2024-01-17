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

		if(title.length()>15){
			context.buildConstraintViolationWithTemplate("이름은 최대 15자까지 입력 가능해요")
					.addConstraintViolation();
			return false;
		}

		if(!title.matches("^[\\S][가-힣a-zA-Z0-9\\s]{0,20}$")){
			context.buildConstraintViolationWithTemplate("특수 문자로는 검색할 수 없어요.")
				.addConstraintViolation();
			return false;
		}

		return true;
	}
}