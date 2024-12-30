package com.eatplatform.web.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;

@Log4j
public class FileUploadUtil {

	public static String subStrName(String fileName) {
		String normalizeName = FilenameUtils.normalize(fileName);
		int dotIndex = normalizeName.lastIndexOf('.');
		
		String realName = normalizeName.substring(0, dotIndex);
		return realName;
	}
	
	public static String subStrExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		// '.' 이후의 문자열을 확장자로 추출
		String extension = fileName.substring(dotIndex + 1);
		return extension;
	}
	
	public static String makeDatePath() {
		Calendar calendar = Calendar.getInstance();
		
		String yearPath = String.valueOf(calendar.get(Calendar.YEAR));
		log.info("yearPath: " + yearPath);
		
		String monthPath = yearPath
				+ File.separator
				+ new DecimalFormat("00")
					.format(calendar.get(Calendar.MONTH) + 1);
		log.info("monthPath: " + monthPath);
		
		String datePath = monthPath
				+ File.separator
				+ new DecimalFormat("00")
					.format(calendar.get(Calendar.DATE));
		log.info("datePath: " + datePath);
		
		return datePath;
	}
	
	public static void saveFile(String uploadPath, MultipartFile file, String uuid) {
		File realUploadPath = new File(uploadPath, makeDatePath());
		if(!realUploadPath.exists()) {
			realUploadPath.mkdirs();
			log.info(realUploadPath.getPath() + " successfully created.");
		} else {
			log.info(realUploadPath.getPath() + " already exists.");
		}
		
		File saveFile = new File(uploadPath, uuid);
			try {
				file.transferTo(saveFile);
				log.info("file upload success");
			} catch (IllegalStateException e) {
				log.error(e.getMessage());
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			
	}
	
	public static void deleteFile(String uploadPath, String path, String chgName) {
		// 삭제할 파일의 전체 경로 생성
		String fullPath = uploadPath + File.separator + path + File.separator + chgName;
		
		// 파일 객체 생성
		File file = new File(fullPath);
		
		// 파일이 존재하는지 확인하고 삭제
		if(file.exists()) {
			if(file.delete()) {
				System.out.println(fullPath + " file delete success.");
			} else {
				System.out.println(fullPath + " file delete failed.");
			}
		} else {
			System.out.println(fullPath + " file not found.");
		}
		
	}

}
