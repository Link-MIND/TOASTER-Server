package com.app.toaster.service.category;

import com.app.toaster.controller.request.category.*;
import com.app.toaster.controller.response.toast.ToastDto;
import com.app.toaster.controller.response.toast.ToastFilter;
import com.app.toaster.controller.response.category.CategoriesReponse;
import com.app.toaster.controller.response.category.GetCategoryResponseDto;
import com.app.toaster.domain.Category;
import com.app.toaster.domain.Toast;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ToastRepository toastRepository;

    @Transactional
    public void createCategory(Long userId, CreateCategoryDto createCategoryDto){
        User presentUser = findUser(userId);

        // 현재 최대 우선순위를 가져와서 새로운 우선순위를 설정
        int maxPriority = categoryRepository.findMaxPriority();

        //카테고리 생성
        Category newCategory = Category.builder()
                .title(createCategoryDto.categoryTitle())
                .user(presentUser)
                .priority(maxPriority + 1)
                .build();

        categoryRepository.save(newCategory);
    }

    @Transactional
    public void deleteCategory(Long userId, DeleteCategoryDto deleteCategoryDto){

        toastRepository.updateCategoryIdsToNull(deleteCategoryDto.deleteCategoryList());
        for (Long categoryId : deleteCategoryDto.deleteCategoryList()) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_CATEGORY_EXCEPTION, Error.NOT_FOUND_CATEGORY_EXCEPTION.getMessage()));

            categoryRepository.decreasePriorityNextDeleteCategory(categoryId, category.getPriority());
        }

        categoryRepository.deleteALLByCategoryIdInQuery(deleteCategoryDto.deleteCategoryList());

    }

    public List<CategoriesReponse> getCategories(Long userId){
        User presentUser = findUser(userId);

        return categoryRepository.findAllByUserOrderByPriority(presentUser)
                .stream()
                .map(category -> CategoriesReponse.builder()
                        .CategoryId(category.getCategoryId())
                        .categoryTitle(category.getTitle())
                        .toastNum(toastRepository.getAllByCategory(category).size()).build()
                ).collect(Collectors.toList());

    }

    @Transactional
    public void editCategories(Long userId, EditCategoryRequestDto editCategoryRequestDto){
        //제목 업데이트
        editCategoryRequestDto.changeCategoryTitleList().forEach(request ->
                categoryRepository.updateCategoryTitle(request.categoryId(), request.newTitle()));

        //순서 업데이트
        for (ChangeCateoryPriorityDto changeCateoryPriorityDto : editCategoryRequestDto.changeCategoryPriorityList()) {
            Long categoryId = changeCateoryPriorityDto.categoryId();
            int newPriority = changeCateoryPriorityDto.newPriority();

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_CATEGORY_EXCEPTION, Error.NOT_FOUND_CATEGORY_EXCEPTION.getMessage()));

            int currentPriority = category.getPriority();
            category.updateCategoryPriority(changeCateoryPriorityDto.newPriority());

            if(currentPriority < newPriority)
                categoryRepository.decreasePriorityByOne(categoryId, currentPriority, newPriority);
            else if (currentPriority > newPriority)
                categoryRepository.increasePriorityByOne(categoryId, currentPriority, newPriority);


        }

    }

    public GetCategoryResponseDto getCategory(Long userId, Long categoryId, ToastFilter filter) {
        User presentUser = findUser(userId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_CATEGORY_EXCEPTION, Error.NOT_FOUND_CATEGORY_EXCEPTION.getMessage()));

        ArrayList<Toast> toasts = toastRepository.getAllByCategory(category);
        List<ToastDto> toastListDto = mapToToastDtoList(toasts, filter, category);

        return GetCategoryResponseDto.builder().allToastNum(toasts.size()).toastListDto(toastListDto).build();
    }

    //해당 유저 탐색
    private User findUser(Long userId){
        return userRepository.findByUserId(userId).orElseThrow(
                ()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
        );
    }

    private List<ToastDto> mapToToastDtoList(List<Toast> toasts, ToastFilter filter, Category category) {
        Stream<Toast> toastStream;

        switch (filter) {
            case ALL:
                toastStream = toasts.stream();
                break;
            case READ:
                toastStream = toastRepository.findByIsReadAndCategory(true, category).stream();
                break;
            case UNREAD:
                toastStream = toastRepository.findByIsReadAndCategory(false, category).stream();
                break;
            default:
                throw new NotFoundException(Error.NOT_FOUND_TOAST_FILTER, Error.NOT_FOUND_TOAST_FILTER.getMessage());
        }

        return toastStream.map(ToastDto::of).toList();
    }
}
