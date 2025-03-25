package com.eatplatform.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eatplatform.web.domain.StoreImageVO;
import com.eatplatform.web.service.StoreImageService;
import com.eatplatform.web.util.FileUploadUtil;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping(value = "/store/image")
@Log4j
public class StoreImageRESTController {

	@Autowired
	private String uploadStoreImgPath;

	@Autowired
	private StoreImageService storeImageService;

	/**
	 * 가게 이미지 첨부
	 * 
	 * @param storeImageVO
	 * @return
	 */
	@PostMapping
	public ResponseEntity<ArrayList<StoreImageVO>> createStoreImage(MultipartFile[] files) {

		ArrayList<StoreImageVO> list = new ArrayList<>();

		for (MultipartFile file : files) {

			// UUID 생성
			String chgName = UUID.randomUUID().toString();
			// 파일 저장
			FileUploadUtil.saveFile(uploadStoreImgPath, file, chgName);

			String path = FileUploadUtil.makeDatePath();
			String extension = FileUploadUtil.subStrExtension(file.getOriginalFilename());

			FileUploadUtil.createThumbnail(uploadStoreImgPath, path, chgName, extension);

			StoreImageVO storeImageVO = new StoreImageVO();

			// 파일 경로 설정
			storeImageVO.setStoreImagePath(path);
			// 파일 실제 이름 설정
			storeImageVO.setStoreImageRealName(FileUploadUtil.subStrName(file.getOriginalFilename()));
			// 파일 변경 이름(UUID) 설정
			storeImageVO.setStoreImageChgName(chgName);
			// 파일 확장자 설정
			storeImageVO.setStoreImageExtension(extension);

			list.add(storeImageVO);
		}

		return new ResponseEntity<ArrayList<StoreImageVO>>(list, HttpStatus.OK);
	}

	/**
	 * 전송받은 파일 경로 및 파일 이름, 확장자로 이미지 파일을 호출
	 * 
	 * @param storeImagePath
	 * @param storeImageChgName
	 * @param storeImageExtension
	 * @return
	 */
	@GetMapping("/display")
	public ResponseEntity<byte[]> displayStoreImg(String storeImagePath, String storeImageChgName,
			String storeImageExtension) {
		log.info("display()");
		ResponseEntity<byte[]> entity = null;

		try {
			// 파일을 읽어와서 byte 배열로 변환
			log.info(storeImagePath + storeImageChgName + storeImageExtension);
			String savedPath = uploadStoreImgPath + File.separator + storeImagePath + File.separator
					+ storeImageChgName;

			if (storeImageChgName.startsWith("t_")) { // 섬네일 파일에는 확장자 추가
				savedPath += "." + storeImageExtension;
			}
			Path path = Paths.get(savedPath);
			byte[] imageBytes = Files.readAllBytes(path);

			Path extensionPath = Paths.get("." + storeImageExtension);
			// 이미지의 MIME 타입 확인하여 적절한 Content-Type 지정
			String contentType = Files.probeContentType(extensionPath);

			// HTTP 응답에 byte 배열과 Content-Type을 설정하여 전송
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.parseMediaType(contentType));
			entity = new ResponseEntity<byte[]>(imageBytes, httpHeaders, HttpStatus.OK);

		} catch (IOException e) {
			// 파일을 읽는 중에 예외 발생 시 예외 처리
			e.printStackTrace();
			return ResponseEntity.notFound().build(); // 파일을 찾을 수 없음을 클라이언트에게 알림
		}
		return entity;
	}

	@GetMapping("/get/{storeImageId}/storeImageExtension/{storeImageExtension}")
	public ResponseEntity<byte[]> getImage(
			@PathVariable("storeImageId") int storeImageId,
			@PathVariable("storeImageExtension") String storeImageExtension) {
		log.info("getImage()");
		
		ResponseEntity<byte[]> entity = null;
		
		StoreImageVO storeImageVO = storeImageService.getStoreImageById(storeImageId);
		
			try {
				// 파일을 읽어와서 byte 배열로 변환
				String savedPath = uploadStoreImgPath + File.separator 
						+ storeImageVO.getStoreImagePath() + File.separator
						+ storeImageVO.getStoreImageChgName(); 
				
				Path path = Paths.get(savedPath);
				byte[] imageBytes = Files.readAllBytes(path);
				
				Path extensionPath = Paths.get("." + storeImageVO.getStoreImageExtension());
				// 이미지의 MIME 타입 확인하여 적절한 Content-Type 지정
				String contentType = Files.probeContentType(extensionPath);
				
				// HTTP 응답에 byte 배열과 Content-Type을 설정하여 전송
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(MediaType.parseMediaType(contentType));
				entity = new ResponseEntity<byte[]>(imageBytes, httpHeaders, HttpStatus.OK);
			} catch (IOException e) {
				// 파일을 읽는 중에 예외 발생 시 예외 처리
				e.printStackTrace();
				return ResponseEntity.notFound().build(); // 파일을 찾을 수 없음을 클라이언트에게 알림
			}
			return entity;
		
	}

	/**
	 * 섬네일 및 원본 이미지 삭제 기능
	 * 
	 * @param storeImagePath
	 * @param storeImageChgName
	 * @param storeImageExtension
	 * @return
	 */
	@PostMapping("/delete")
	public ResponseEntity<Integer> deleteStoreImg(String storeImagePath, String storeImageChgName,
			String storeImageExtension) {
		log.info("deleteImage()");
		log.info(storeImageChgName);
		FileUploadUtil.deleteFile(uploadStoreImgPath, storeImagePath, storeImageChgName);

		String thumbnailName = "t_" + storeImageChgName + "." + storeImageExtension;
		FileUploadUtil.deleteFile(uploadStoreImgPath, storeImagePath, thumbnailName);

		return new ResponseEntity<Integer>(1, HttpStatus.OK);

	}

}
