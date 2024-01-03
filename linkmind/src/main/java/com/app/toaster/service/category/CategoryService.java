package com.app.toaster.service.category;

import com.app.toaster.controller.request.category.EditCategoryDto;
import com.app.toaster.controller.request.category.CreateCategoryDto;
import com.app.toaster.controller.request.category.DeleteCategoryDto;
import com.app.toaster.controller.request.category.EditCategoryListDto;
import com.app.toaster.controller.response.category.CategoriesReponse;
import com.app.toaster.domain.Category;
import com.app.toaster.domain.User;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.exception.model.UnauthorizedException;
import com.app.toaster.infrastructure.CategoryRepository;
import com.app.toaster.infrastructure.ToastRepository;
import com.app.toaster.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ToastRepository toastRepository;

    @Transactional
    public void createCategory(Long userId, CreateCategoryDto createCategoryDto){
        User presentUser = findUser(userId);

        //카테고리 생성
        Category newCategory = Category.builder()
                .title(createCategoryDto.categoryTitle())
                .user(presentUser)
                .build();
        categoryRepository.save(newCategory);
    }

    @Transactional
    public void deleteCategory(Long userId, DeleteCategoryDto deleteCategoryDto){
        User presentUser = findUser(userId);

        for(Long categoryId : deleteCategoryDto.deleteCategoryList()){
            Category category = categoryRepository.findById(categoryId).orElseThrow(
                    () -> new NotFoundException(Error.NOT_FOUND_CATEGORY_EXCEPTION, Error.NOT_FOUND_CATEGORY_EXCEPTION.getMessage())
            );
            //접속 유저가 만든 카테고리가 아닌 경우
            if (!presentUser.equals(category.getUser())){
                throw new UnauthorizedException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
            }

            categoryRepository.deleteByCategoryId(categoryId);
        }
    }

    public List<CategoriesReponse> getCategories(Long userId){
        User presentUser = findUser(userId);

        return categoryRepository.findAllByUser(presentUser)
                .stream()
                .map(category -> CategoriesReponse.builder()
                        .CategoryId(category.getCategoryId())
                        .categoryTitle(category.getTitle())
                        .toastNum(toastRepository.getAllByCategory(category).size()).build()
                ).collect(Collectors.toList());

    }

    @Transactional
    public void editCategories(Long userId, EditCategoryListDto editCategoryListDto){
        User presentUser = findUser(userId);

        for(EditCategoryDto editCategoryDto : editCategoryListDto.editCategoryListDto()){
            Category category = categoryRepository.findById(editCategoryDto.categoryId()).orElseThrow(
                    () -> new NotFoundException(Error.NOT_FOUND_CATEGORY_EXCEPTION, Error.NOT_FOUND_CATEGORY_EXCEPTION.getMessage())
            );
            //접속 유저가 만든 카테고리가 아닌 경우
            if (!presentUser.equals(category.getUser())){
                throw new UnauthorizedException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
            }

            category.updateCategoryName(editCategoryDto.newTitle());
        }
    }

    //해당 유저 탐색
    private User findUser(Long userId){
        return userRepository.findByUserId(userId).orElseThrow(
                ()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
        );
    }
}
