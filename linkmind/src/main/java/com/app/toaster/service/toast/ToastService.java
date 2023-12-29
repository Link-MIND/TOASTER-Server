package com.app.toaster.service.toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.controller.request.toast.SaveToastDto;
import com.app.toaster.domain.Category;
import com.app.toaster.domain.CategoryManagement;
import com.app.toaster.domain.Toast;
import com.app.toaster.domain.User;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.infrastructure.CategoryManagementRepository;
import com.app.toaster.infrastructure.CategoryRepository;
import com.app.toaster.infrastructure.ToastRepository;
import com.app.toaster.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ToastService {
	private final UserRepository userRepository;
	private final ToastRepository toastRepository;
	private final CategoryRepository categoryRepository;
	private final CategoryManagementRepository categoryManagementRepository;

	public void createToast(Long userId, SaveToastDto saveToastDto){
		//해당 유저 탐색
		User presentUser =  userRepository.findByUserId(userId).orElseThrow(
			()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
		);
		//토스트 생성
		Toast toast = Toast.builder()
			.user(presentUser)
			.linkUrl(saveToastDto.linkUrl())
			.title(saveToastDto.title())
			.build();
		toastRepository.save();
		// 카테고리를 선택하지 않았을 경우 기본 값 세팅 예시 -> 키워드 1
		List<CategoryManagement> categoryManagements = new ArrayList<>();

		if (saveToastDto.categoryIds() == null || saveToastDto.categoryIds().isEmpty()) {
			CategoryManagement categoryManagement = CategoryManagement.builder()
				.toast(toast)
				.category(createDefaultCategory())
				.build();
			categoryManagements.add(categoryManagement);
			categoryManagementRepository.saveAll(categoryManagements);
			toast.updateCategories(categoryManagements);
			return;
		}
		//원래 있던 곳에서 카테고리 고르면 카테고리 아이디로 검색 후 그것을 넣음.
		for (Long categoryId : saveToastDto.categoryIds()) {
			Category category = categoryRepository.findById(categoryId).orElse(null);
			if (category != null) {
				CategoryManagement categoryManagement = CategoryManagement.builder()
					.toast(toast)
					.category(category)
					.build();
				categoryManagementRepository.saveAll(categoryManagements);
			}
		}
		toast.updateCategories(categoryManagements);
	}
	// 만약 유저에게 만들어져있는 카테고리가 없는지 확인하고 없으면 기본 카테고리 만듬.
	private Category createDefaultCategory(){
		if (categoryRepository.count()==0){
			Category category = Category.builder()
				.title("기본 카테고리")
				.build();
			return category;
		}
		return null;
	}
}
