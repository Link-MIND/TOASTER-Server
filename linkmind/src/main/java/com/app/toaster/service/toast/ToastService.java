package com.app.toaster.service.toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.app.toaster.controller.request.toast.IsReadDto;
import com.app.toaster.controller.request.toast.SaveToastDto;
import com.app.toaster.controller.response.toast.IsReadResponse;
import com.app.toaster.domain.Category;
import com.app.toaster.domain.Toast;
import com.app.toaster.domain.User;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.BadRequestException;
import com.app.toaster.exception.model.CustomException;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.exception.model.UnauthorizedException;
import com.app.toaster.external.client.aws.S3Service;
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
	private final S3Service s3Service;
	private static final String TOAST_IMAGE_FOLDER_NAME = "toast/";

	@Value("${static-image.root}")
	private String BASIC_ROOT;


	@Transactional
	public void createToast(Long userId, SaveToastDto saveToastDto, MultipartFile image){
		//해당 유저 탐색
		User presentUser =  userRepository.findByUserId(userId).orElseThrow(
			()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
		);
		//토스트 생성
		try {
			final String imageUrl = s3Service.uploadImage(image,TOAST_IMAGE_FOLDER_NAME);
			Toast toast = Toast.builder()
				.user(presentUser)
				.linkUrl(saveToastDto.linkUrl())
				.title(saveToastDto.title())
				.thumbnailUrl(BASIC_ROOT+imageUrl)
				.build();
			// 만약 유저에게 만들어져있는 카테고리가 없는지 확인하고
			checkCategoryIsEmpty(toast, saveToastDto.categoryId());
			toastRepository.save(toast);
		} catch (RuntimeException e) {	//여기서 에러 발생 시 외부 s3 문제일 수 도 있으므로 500으로 에러 예상 범위 알림.
			throw new CustomException(Error.CREATE_TOAST_PROCCESS_EXCEPTION, Error.CREATE_TOAST_PROCCESS_EXCEPTION.getMessage());
		}

	}

	@Transactional
	public IsReadResponse readToast(Long userId, IsReadDto isReadDto){
		User presentUser =  userRepository.findByUserId(userId).orElseThrow(
			()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
		);
		Toast toast = toastRepository.findById(isReadDto.toastId()).orElseThrow(
			() -> new NotFoundException(Error.NOT_FOUND_TOAST_EXCEPTION, Error.NOT_FOUND_TOAST_EXCEPTION.getMessage())
		);
		if (!presentUser.equals(toast.getUser())){
			throw new UnauthorizedException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
		}
		if (isReadDto.isRead() && toast.getIsRead() || !isReadDto.isRead() && !toast.getIsRead()){
			throw new BadRequestException(Error.BAD_REQUEST_ISREAD, Error.BAD_REQUEST_ISREAD.getMessage());
		}
		toast.updateIsRead(isReadDto.isRead());
		return IsReadResponse.of(isReadDto.isRead());
	}

	@Transactional
	public void deleteToast(Long userId, Long toastId) throws IOException {
		User presentUser =  userRepository.findByUserId(userId).orElseThrow(
			()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
		);
		Toast toast = toastRepository.findById(toastId).orElseThrow(
			() -> new NotFoundException(Error.NOT_FOUND_TOAST_EXCEPTION, Error.NOT_FOUND_TOAST_EXCEPTION.getMessage())
		);
		if (!presentUser.equals(toast.getUser())){
			throw new UnauthorizedException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
		}
		// BASIC_ROOT를 제외한 문자열을 추출합니다.
		String imageKey = toast.getThumbnailUrl().replace(BASIC_ROOT, "");
		s3Service.deleteImage(imageKey);
		toastRepository.deleteById(toastId);
	}

	@Transactional
	public void deleteAllToast(User user) throws IOException {
		List<Toast> toasts = toastRepository.getAllByUser(user);
		// BASIC_ROOT를 제외한 문자열을 추출합니다.
		List<String> imageKeyList = toasts.stream().map(
			(toast) -> toast.getThumbnailUrl().replace(BASIC_ROOT, ""))
			.collect(Collectors.toList());
		s3Service.deleteImages(imageKeyList);
		toastRepository.deleteAllById(toasts.stream().map(
			(toast -> toast.getId())).collect(Collectors.toList()));
	}

	private void checkCategoryIsEmpty(Toast toast, Long categoryId){
		if (categoryId == null) {
			toast.updateCategory(null);
		} else { //원래 있던 곳에서 카테고리 고르면 카테고리 아이디로 검색 후 그것을 넣음.
			Category foundCategory = categoryRepository.findById(categoryId).orElseThrow(
				() -> new CustomException(Error.NOT_FOUND_CATEGORY_EXCEPTION,
					Error.NOT_FOUND_CATEGORY_EXCEPTION.getMessage()));
			toast.updateCategory(foundCategory);
		}
	}
}
