package com.app.toaster.controller;

import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.controller.request.category.CreateCategoryDto;
import com.app.toaster.controller.request.category.DeleteCategoryDto;
import com.app.toaster.controller.request.toast.IsReadDto;
import com.app.toaster.controller.request.toast.SaveToastDto;
import com.app.toaster.controller.response.category.CategoriesReponse;
import com.app.toaster.controller.response.toast.IsReadResponse;
import com.app.toaster.exception.Success;
import com.app.toaster.infrastructure.CategoryRepository;
import com.app.toaster.service.category.CategoryService;
import com.app.toaster.service.toast.ToastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createCateory(
            @RequestHeader("userId") Long userId,
            @RequestBody CreateCategoryDto createCategoryDto
    ){
        categoryService.createCategory(userId, createCategoryDto);
        return ApiResponse.success(Success.CREATE_CATEGORY_SUCCESS);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteCategory(
            @RequestHeader("userId") Long userId,
            @RequestBody DeleteCategoryDto deleteCategoryDto
    ){
        categoryService.deleteCategory(userId, deleteCategoryDto);
        return ApiResponse.success(Success.DELETE_CATEGORY_SUCCESS);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<CategoriesReponse>> getCategories(@RequestHeader("userId") Long userId){
        return ApiResponse.success(Success.GET_CATEORIES_SUCCESS, categoryService.getCategories(userId));
    }
}
