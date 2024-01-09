package com.app.toaster.controller;

import com.app.toaster.common.dto.ApiResponse;
import com.app.toaster.controller.request.category.*;
import com.app.toaster.controller.response.toast.ToastFilter;
import com.app.toaster.controller.response.category.CategoriesReponse;
import com.app.toaster.controller.response.category.GetCategoryResponseDto;
import com.app.toaster.exception.Success;
import com.app.toaster.service.category.CategoryService;
import com.app.toaster.service.search.SearchService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final SearchService searchService;

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

    @PatchMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse editCategories(
            @RequestHeader("userId") Long userId,
            @RequestBody EditCategoryRequestDto editCategoryRequestDto
    ){
        categoryService.editCategories(userId, editCategoryRequestDto);
        return ApiResponse.success(Success.UPDATE_CATEGORY_TITLE_SUCCESS);
    }

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<GetCategoryResponseDto> getCategory(
            @RequestHeader("userId") Long userId,
            @PathVariable Long categoryId,
            @RequestParam("filter") ToastFilter filter
    ){
        return ApiResponse.success(Success.GET_CATEORY_SUCCESS,categoryService.getCategory(userId, categoryId, filter));
    }


    @GetMapping("/search")
    public ApiResponse searchProducts(@RequestHeader Long userId ,@RequestParam("query") String query){
      return searchService.searchMain(userId,query);
    }

}
