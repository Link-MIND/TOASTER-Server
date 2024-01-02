package com.app.toaster.service.category;

import com.app.toaster.controller.request.category.CreateCategoryDto;
import com.app.toaster.controller.request.toast.SaveToastDto;
import com.app.toaster.domain.Category;
import com.app.toaster.domain.Toast;
import com.app.toaster.domain.User;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.infrastructure.CategoryRepository;
import com.app.toaster.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void createCategory(Long userId, CreateCategoryDto createCategoryDto){
        //해당 유저 탐색
        User presentUser = userRepository.findByUserId(userId).orElseThrow(
                ()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
        );
        //카테고리 생성
        Category newCategory = Category.builder()
                .title(createCategoryDto.categoryTitle())
                .user(presentUser)
                .build();
        categoryRepository.save(newCategory);
    }
}
