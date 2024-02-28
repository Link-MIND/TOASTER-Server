package com.app.toaster.service.toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.toaster.controller.request.toast.IsReadDto;
import com.app.toaster.controller.request.toast.SaveToastDto;
import com.app.toaster.controller.request.toast.UpdateToastDto;
import com.app.toaster.controller.response.parse.OgResponse;
import com.app.toaster.controller.response.toast.IsReadResponse;
import com.app.toaster.controller.response.toast.ModifiedTitle;
import com.app.toaster.domain.Category;
import com.app.toaster.domain.Toast;
import com.app.toaster.domain.User;
import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.BadRequestException;
import com.app.toaster.exception.model.CustomException;
import com.app.toaster.exception.model.ForbiddenException;
import com.app.toaster.exception.model.NotFoundException;
import com.app.toaster.external.client.aws.ImagePresignedUrlResponse;
import com.app.toaster.external.client.aws.PresignedUrlVO;
import com.app.toaster.external.client.aws.S3Service;
import com.app.toaster.infrastructure.CategoryRepository;
import com.app.toaster.infrastructure.ToastRepository;
import com.app.toaster.infrastructure.UserRepository;
import com.app.toaster.service.parse.ParsingServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
// @Slf4j
public class ToastService {
	private final UserRepository userRepository;
	private final ToastRepository toastRepository;
	private final CategoryRepository categoryRepository;
	private final S3Service s3Service;
	private final ParsingServiceImpl parsingService;
	private static final String TOAST_IMAGE_FOLDER_NAME = "toast/";

	@Value("${static-image.root}")
	private String BASIC_ROOT;


	@Transactional
	public void createToast(Long userId, SaveToastDto saveToastDto){
		//해당 유저 탐색
		User presentUser =  userRepository.findByUserId(userId).orElseThrow(
			()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
		);
		//토스트 생성
		try {
			System.out.println(saveToastDto.linkUrl());
			OgResponse res = parsingService.getOg(saveToastDto.linkUrl());
			// //byte 배열로 읽어들임.
			// log.info(res.titleAdvanced());
			// log.info(res.imageAdvanced());
			String imageString = checkIsBasicImage(res.imageAdvanced());
			// // ImagePresignedUrlResponse realRes = getUploadPreSignedUrl(res.imageAdvanced());
			// log.info(realRes.fileName());
			// log.info(realRes.preSignedUrl());

			//presigned url
			Toast toast = Toast.builder()
				.user(presentUser)
				.linkUrl(saveToastDto.linkUrl())
				.title(res.titleAdvanced())
				.thumbnailUrl(imageString)
				.build();
			// 만약 유저에게 만들어져있는 카테고리가 없는지 확인하고
			checkCategoryIsEmpty(toast, saveToastDto.categoryId());
			toastRepository.save(toast);
		} catch (IOException e ) {	//여기서 에러 발생 시 외부 s3 문제일 수 도 있으므로 500으로 에러 예상 범위 알림.
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
			throw new ForbiddenException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
		}
		if (isReadDto.isRead() && toast.getIsRead() || !isReadDto.isRead() && !toast.getIsRead()){
			throw new BadRequestException(Error.BAD_REQUEST_ISREAD, Error.BAD_REQUEST_ISREAD.getMessage());
		}

		if(isReadDto.isRead())
			toast.setUpdateAt();

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
			throw new ForbiddenException(Error.UNAUTHORIZED_ACCESS, Error.UNAUTHORIZED_ACCESS.getMessage());
		}
		// BASIC_ROOT를 제외한 문자열을 추출합니다.
		// String imageKey = toast.getThumbnailUrl().replace(BASIC_ROOT, "");
		// s3Service.deleteImage(imageKey);
		toastRepository.deleteById(toastId);
	}

	@Transactional
	public void deleteAllToast(User user) throws IOException {
		List<Toast> toasts = toastRepository.getAllByUser(user);
		// // BASIC_ROOT를 제외한 문자열을 추출합니다.
		// List<String> imageKeyList = toasts.stream().map(
		// 	(toast) -> toast.getThumbnailUrl().replace(BASIC_ROOT, ""))
		// 	.collect(Collectors.toList());
		// s3Service.deleteImages(imageKeyList);
		toastRepository.deleteAllById(toasts.stream().map(
			(Toast::getId)).collect(Collectors.toList()));
	}

	@Transactional
	public ModifiedTitle modifyTitle(Long userId, UpdateToastDto updateToastDto){
		User presentUser =  userRepository.findByUserId(userId).orElseThrow(
			()-> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
		);
		Toast toast = toastRepository.findById(updateToastDto.toastId()).orElseThrow(
			() -> new BadRequestException(Error.BAD_REQUEST_ID, Error.BAD_REQUEST_ID.getMessage())
		);
		if (!presentUser.equals(toast.getUser())){
			throw new ForbiddenException(Error.UNAUTHORIZED_ACCESS, Error.UNAUTHORIZED_ACCESS.getMessage());
		}
		toast.updateTitle(updateToastDto.title());
		return ModifiedTitle.of(updateToastDto.title());

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
	// presigned url로 저장하는 로직
	// private void convertToBytes(){
	// 	// RestClient restClient = RestClient.create(BASIC_ROOT+res.imageAdvanced());
	// 	// Response response = restClient.get();
	// 	// byte[] imageBytes = response.content().asByteArray();
	// }

	// public ImagePresignedUrlResponse getUploadPreSignedUrl(String filename) {
	// 	try {
	//
	// 		PresignedUrlVO presignedUrlVO = s3Service.getUploadPreSignedUrl(filename ,TOAST_IMAGE_FOLDER_NAME);
	// 		return new ImagePresignedUrlResponse(
	// 			presignedUrlVO.fileName(),
	// 			presignedUrlVO.url()
	// 		);
	// 	} catch (Exception e) {
	//
	// 		System.out.println(e.getMessage());
	// 		System.out.println(String.valueOf(e.getCause()));
	// 		System.out.println(Arrays.toString(e.getStackTrace()));
	// 		throw e;
	// 	}
	// }
	// presign url로 요청 보내는 api
	private void requestPreSignedUrl(){
		// HttpHeaders headers = new HttpHeaders();
		// headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		// final String imageUrl = TOAST_IMAGE_FOLDER_NAME+realRes.fileName();
		// s3Service.getURL(imageUrl);
		//
		// HttpEntity<byte[]> entity = new HttpEntity<>(bytes, headers);
		//
		// // RestTemplate을 사용하여 PUT 요청을 보낸다.
		// RestTemplate restTemplate = new RestTemplate();
		// restTemplate.put(realRes.preSignedUrl(), entity);
	}

	private String checkIsBasicImage(String imageUrl){
		if (!imageUrl.startsWith("http")){
			return BASIC_ROOT+imageUrl;
		}
		return imageUrl;
	}




}
