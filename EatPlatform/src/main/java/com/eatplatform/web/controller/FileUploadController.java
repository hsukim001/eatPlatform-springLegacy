package com.eatplatform.web.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping(value = "/page/image")
@Log4j
public class FileUploadController {

	@Autowired
	private String uploadPath; // Bean으로 설정된 uploadPath() 객체 주입
	
	@GetMapping("/upload")
	public void uploadGET() {
		log.info("uploadGET()");
	}

	@PostMapping("/upload")
	public void uploadPOST(MultipartFile file) { // 전송된 파일 객체 저장
		log.info("uploadPost()");
		log.info("파일 이름 : " + file.getOriginalFilename());
		log.info("파일 크기 : " + file.getSize());
	      
	    // File 객체에 파일 경로 및 파일명 설정
	    File savedFile = new File(uploadPath, file.getOriginalFilename());
	    try {
	    	file.transferTo(savedFile); // 실제 경로에 파일 저장
	    	log.info(savedFile.getAbsolutePath());
	    } catch (Exception e) {
	    	log.error(e.getMessage());
	    } 
	 }
	
	// 다중 파일 업로드 수신 및 파일들 저장
	@PostMapping("/uploads")
	public void uploadsPost(MultipartFile[] files) { // 배열에 전송된 파일들 적용
		for(MultipartFile file : files) {
			log.info(file.getOriginalFilename());
	        File savedFile = new File(uploadPath, file.getOriginalFilename());
	        try {
	        	file.transferTo(savedFile); // 실제 경로에 파일 저장
	        } catch (Exception e) {
	        	log.error(e.getMessage());
	        } 
	     }
	 }

}
