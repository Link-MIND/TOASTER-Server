package com.app.toaster.toast.service;

import com.app.toaster.category.domain.Category;
import com.app.toaster.category.infrastructure.CategoryRepository;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.CustomException;
import com.app.toaster.parse.controller.response.OgResponse;
import com.app.toaster.parse.service.ParsingService;
import com.app.toaster.toast.controller.request.SaveToastDto;
import com.app.toaster.toast.domain.Toast;
import com.app.toaster.toast.infrastructure.ToastRepository;
import com.app.toaster.user.domain.User;
import com.app.toaster.user.infrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToastServiceTest {

    @Mock
    private ToastRepository toastRepository;

    @Mock
    private ParsingService parsingService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository; // findUser 메소드를 위해 필요할 수 있음

    @InjectMocks
    private ToastService toastService;

    private User testUser;

    private Category testCategory;
    private SaveToastDto validSaveToastDto;
    private OgResponse mockOgResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .socialId("testUser")
                .nickname("test")
                .build();

        validSaveToastDto = new SaveToastDto("https://example.com", 1L);

        mockOgResponse = OgResponse.of("Test Title", "https://example.com/image.jpg");

        testCategory = Category.builder()
                .priority(0)
                .title("hi")
                .user(testUser)
                .build();
    }

    @Test
    @DisplayName("Toast 생성 성공 테스트")
    void createToast_Success() throws IOException {
        // given
        Long userId = 1L;


        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(testUser));
        when(parsingService.getOg(validSaveToastDto.linkUrl())).thenReturn(mockOgResponse);
        when(categoryRepository.findById(userId)).thenReturn(Optional.of(testCategory));

        // when
        toastService.createToast(userId, validSaveToastDto);

        // then
        verify(parsingService, times(1)).getOg(validSaveToastDto.linkUrl());
        verify(toastRepository, times(1)).save(any(Toast.class));

        // Toast 객체가 올바르게 생성되는지 확인
        ArgumentCaptor<Toast> toastCaptor = ArgumentCaptor.forClass(Toast.class);
        verify(toastRepository).save(toastCaptor.capture());

        Toast savedToast = toastCaptor.getValue();
        assertThat(savedToast.getUser()).isEqualTo(testUser);
        assertThat(savedToast.getLinkUrl()).isEqualTo(validSaveToastDto.linkUrl());
        assertThat(savedToast.getTitle()).isEqualTo(mockOgResponse.titleAdvanced());
    }

    @Test
    @DisplayName("URL이 null인 경우 EMPTY_URL 에러를 반환한다.")
    void createToast_Fail_NullUrl() {
        // given
        Long userId = 1L;

        SaveToastDto invalidDto = new SaveToastDto(null, 1L);

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(testUser));

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> toastService.createToast(userId, invalidDto));

        assertThat(exception.getError()).isEqualTo(Error.BAD_REQUEST_EMPTY_URL);
    }

    @Test
    @DisplayName("URL이 빈 문자열인 경우 EMPTY_URL에러를 반환한다.")
    void createToast_Fail_EmptyUrl() {
        // given
        Long userId = 1L;

        SaveToastDto invalidDto = new SaveToastDto("", 1L);
        SaveToastDto invalidDto2 = new SaveToastDto(" ", 1L);


        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(testUser));

        // when & then
        CustomException exception1 = assertThrows(CustomException.class,
                () -> toastService.createToast(userId, invalidDto));
        CustomException exception2 = assertThrows(CustomException.class,
                () -> toastService.createToast(userId, invalidDto2));

        assertThat(exception1.getError()).isEqualTo(Error.BAD_REQUEST_EMPTY_URL);
        assertThat(exception2.getError()).isEqualTo(Error.BAD_REQUEST_EMPTY_URL);

    }



}