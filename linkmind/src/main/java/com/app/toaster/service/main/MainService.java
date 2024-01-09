package com.app.toaster.service.main;

import com.app.toaster.controller.response.category.CategoriesReponse;
import com.app.toaster.controller.response.main.MainPageResponseDto;
import com.app.toaster.domain.Category;
import com.app.toaster.domain.User;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.infrastructure.CategoryRepository;
import com.app.toaster.infrastructure.RecommedSiteRepository;
import com.app.toaster.infrastructure.ToastRepository;
import com.app.toaster.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {

    private final UserRepository userRepository;
    private final ToastRepository toastRepository;
    private final CategoryRepository categoryRepository;
    private final RecommedSiteRepository recommedSiteRepository;


    public MainPageResponseDto getMainPage(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));

        int allToastNum = toastRepository.getAllByUser(user).size();
        int readToastNum = toastRepository.getAllByUserAndIsReadIsTrue(user).size();

        MainPageResponseDto mainPageResponseDto = MainPageResponseDto.builder().nickname(user.getNickname())
                .allToastNum(allToastNum)
                .readToastNum(readToastNum)
                .recommendedSiteListDto(recommedSiteRepository.findAll().subList(0, Math.min(9, recommedSiteRepository.findAll().size())))
                .mainCategoryListDto(getCategory(user).stream()
                .map(category -> CategoriesReponse.builder()
                        .CategoryId(category.getCategoryId())
                        .categoryTitle(category.getTitle())
                        .toastNum(toastRepository.getAllByCategory(category).size()).build()
                ).collect(Collectors.toList())).build();

        return mainPageResponseDto;
    }

    private ArrayList<Category> getCategory(User user){

        ArrayList<Category> categories = categoryRepository.findAllByUser(user);

        if(categories.size()<4)
            return categories;

        return categoryRepository.findTop3ByUserOrderByLatestReadTimeDesc(user);

    }
}
