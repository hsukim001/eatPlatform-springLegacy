package com.eatplatform.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eatplatform.web.domain.ReviewImageVO;
import com.eatplatform.web.service.ReviewImageService;
import com.eatplatform.web.util.FileUploadUtil;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping(value = "/page")
@Log4j
public class ReviewImageController {
	
	@Autowired
	private String uploadPath;
	
	@Autowired
	private ReviewImageService reviewImageService;

	
	// 첨부 파일 업로드 페이지 이동(GET)
	@GetMapping("/register")
	public void registerGET() {
		log.info("registerGET()");
	} // end registerGET()

	// 첨부 파일 업로드 처리(POST)
	@PostMapping("/image")
	public String imagePOST(ReviewImageVO reviewImageVO) {
	      log.info("imagePOST()");
	      MultipartFile file = reviewImageVO.getFile();

	      // UUID 생성
	      String chgName = UUID.randomUUID().toString();
	      // 파일 저장
	      FileUploadUtil.saveFile(uploadPath, file, chgName);

	      // 파일 경로 설정
	      reviewImageVO.setReviewImagePath(FileUploadUtil.makeDatePath());
	      // 파일 실제 이름 설정
	      reviewImageVO.setReviewImageRealName(FileUploadUtil.subStrName(file.getOriginalFilename()));
	      // 파일 변경 이름(UUID) 설정
	      reviewImageVO.setReviewImageChgName(chgName);
	      // 파일 확장자 설정
	      reviewImageVO.setReviewImageExtension(FileUploadUtil.subStrExtension(file.getOriginalFilename()));
	      // DB에 첨부 파일 정보 저장
	      log.info(reviewImageService.createReviewImage(reviewImageVO) + "행 등록");

	      return "redirect:/list";
	   } 
	   
	    // 첨부 파일 목록 조회(GET)
	    @GetMapping("/list")
	    public void list(Model model) {
	        // 첨부 파일 reviewImageId 리스트를 Model에 추가하여 전달
	        model.addAttribute("idList", reviewImageService.getAllId());
	        log.info("list()");
	    }

	    // 첨부 파일 상세 정보 조회(GET)
	    @GetMapping("/imageDetail")
	    public void detail(int reviewImageId, Model model) {
	        log.info("imageDetail()");
	        log.info("reviewImageId : " + reviewImageId);
	        // reviewImageId로 상세 정보 조회
	        ReviewImageVO reviewImageVO = reviewImageService.getReviewImageById(reviewImageId);
	        // 조회된 상세 정보를 Model에 추가하여 전달
	        model.addAttribute("reviewImageVO", reviewImageVO);
	    } // end imageDetail()
	    
	    // 첨부 파일 다운로드(GET)
	    // 파일을 클릭하면 사용자가 다운로드하는 방식
	    // 파일 리소스를 비동기로 전송하여 파일 다운로드
	    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	    @ResponseBody // 페이지를 불러오는게 아니라 데이터를 주는 형태
	    public ResponseEntity<Resource> download(int reviewImageId) throws IOException {
	        log.info("download()");
	        
	        // attachId로 상세 정보 조회
	        ReviewImageVO reviewImageVO = reviewImageService.getReviewImageById(reviewImageId);
	        String reviewImagePath = reviewImageVO.getReviewImagePath();
	        String reviewImageChgName = reviewImageVO.getReviewImageChgName();
	        String reviewImageExtension = reviewImageVO.getReviewImageExtension();
	        String reviewImageRealName = reviewImageVO.getReviewImageRealName();
	        
	        // 서버에 저장된 파일 정보 생성
	        String resourcePath = uploadPath + File.separator + reviewImagePath 
	                                    + File.separator + reviewImageChgName;
	        // 파일 리소스 생성
	        Resource resource = new FileSystemResource(resourcePath);
	        // 다운로드할 파일 이름을 헤더에 설정
	        HttpHeaders headers = new HttpHeaders();
	        String reviewImageName = new String(reviewImageRealName.getBytes("UTF-8"), "ISO-8859-1");
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "reviewImage; filename=" 
	              + reviewImageChgName + "." + reviewImageExtension);

	        // 파일을 클라이언트로 전송
	        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	    } // end download()


	} // end FileUploadController
