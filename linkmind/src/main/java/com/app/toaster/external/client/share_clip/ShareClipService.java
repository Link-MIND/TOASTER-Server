package com.app.toaster.external.client.share_clip;

import com.app.toaster.category.domain.Category;
import com.app.toaster.category.infrastructure.CategoryRepository;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.CustomException;
import com.app.toaster.external.client.share_clip.request.ClipInfoRequestDto;
import com.app.toaster.external.client.share_clip.request.CreateShareClipRequestDto;
import com.app.toaster.external.client.share_clip.request.UserInfoRequestDto;
import com.app.toaster.external.client.share_clip.response.ShareClipResponseDto;
import com.app.toaster.toast.domain.Toast;
import com.app.toaster.toast.infrastructure.ToastRepository;
import com.app.toaster.user.domain.SocialType;
import com.app.toaster.user.domain.User;
import com.app.toaster.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ShareClipService {

    private final CategoryRepository categoryRepository;
    private final ToastRepository toastRepository;
    private final UserRepository userRepository;
    private final static int MAX_CATERGORY_NUMBER = 15;

    @Transactional
    public ShareClipResponseDto createShareClip(CreateShareClipRequestDto createShareClipRequestDto){
        try {
            UserInfoRequestDto userInfoRequestDto = createShareClipRequestDto.userInfoRequestDto();
            ClipInfoRequestDto clipInfoRequestDto = createShareClipRequestDto.clipDto();

            User user = userRepository.findBySocialIdAndSocialType(userInfoRequestDto.receiverSocialId(), SocialType.valueOf(userInfoRequestDto.receiverSocialType()))
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));

            Category category = createCategory(user, clipInfoRequestDto);

            List<Toast> toastList = clipInfoRequestDto.toasts().stream().map(
                (toast) -> Toast.builder()
                    .thumbnailUrl(toast.linkUrl())
                    .linkUrl(toast.linkUrl())
                    .title(toast.title())
                    .category(category)
                    .user(user)
                    .build()
            ).toList();

            toastRepository.saveAll(toastList);
            return ShareClipResponseDto.success(user.getUserId(), category.getCategoryId());
        }catch (Exception e){
            return ShareClipResponseDto.fail(e.getMessage());
        }
    }

    private Category createCategory(User presentUser, ClipInfoRequestDto clipDto){
        val maxPriority = categoryRepository.findMaxPriorityByUser(presentUser);

        val categoryNum = categoryRepository.countAllByUser(presentUser);
        System.out.println(categoryNum);

        if (categoryNum >= MAX_CATERGORY_NUMBER) {
            throw new CustomException(Error.BAD_REQUEST_CREATE_CLIP_EXCEPTION,
                Error.BAD_REQUEST_CREATE_CLIP_EXCEPTION.getMessage());
        }

        if(categoryRepository.countAllByTitleAndUser(clipDto.clipTitle(),presentUser)>0){
            throw new CustomException(Error.UNPROCESSABLE_CREATE_TIMER_EXCEPTION, Error.UNPROCESSABLE_CREATE_TIMER_EXCEPTION.getMessage());
        }

        //카테고리 생성
        Category newCategory = Category.builder()
            .title(clipDto.clipTitle())
            .user(presentUser)
            .priority(maxPriority + 1)
            .build();
        return categoryRepository.save(newCategory);
    }

}
