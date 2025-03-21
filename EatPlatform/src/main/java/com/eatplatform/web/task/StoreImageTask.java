package com.eatplatform.web.task;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eatplatform.web.domain.StoreImageVO;
import com.eatplatform.web.persistence.StoreImageMapper;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class StoreImageTask {
	
	@Autowired
	private String uploadStoreImgPath;
	
	@Autowired
	private StoreImageMapper storeImageMapper;
	
	@Scheduled(cron = "0 0 11 * * *")  // 매일 11시에 실행
	
		public static String getFormattedDateString() {
		
			log.info("getFormattedDateString()");
			
			// 현재 날짜에서 1일 전 날짜를 구함
			LocalDate yesterday = LocalDate.now().minusDays(1);
			log.info("yesterday:" + yesterday);
			
			// 날짜 형식을 "yyyy/MM/dd" 형식으로 설정
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			
			return yesterday.format(formatter);
		
		}
	
	@Scheduled(cron = "0 0 11 * * *")  // 매일 11시에 실행
	
		public void deleteStoreImages() {
			log.warn("===========================");
			log.warn("Delete StoreImage Task Run");
			
			Path storeImagePath = Paths.get(getFormattedDateString().replace("/", "\\"));
			if(Files.notExists(storeImagePath)) {
				log.info("storeImagePath : " + storeImagePath);
				log.info("이미지 파일 경로가 존재하지 않습니다.");
				return;
			}
			
			// 1일전 날짜의 첨부 파일 경로에 해당하는 파일 정보 조회
			List<StoreImageVO> storeImageList = storeImageMapper.selectListByStoreImagePath(getFormattedDateString().replace("/", "\\"));
			log.info("storeImageList : " + storeImageList);
			
			// 전날 이미지 데이터가 0개인데, 파일은 남아있을 때
			if (storeImageList.size() == 0) {
				File targetDir = Paths.get(uploadStoreImgPath, getFormattedDateString()).toFile();
				
				File[] removeFiles = targetDir.listFiles();
				
				for (File file : removeFiles) {
					log.warn(file.getAbsolutePath());
					file.delete(); // 파일 삭제
					log.info("이미지 파일 삭제");
				}
				return;
				
			}
			
			 // 파일 정보에서 파일 이름만 추출하여 List<String>으로 변경
	         List<String> savedList = new ArrayList<>();
	         
	         for(StoreImageVO storeImageVO : storeImageList) {
	            savedList.add(toChgName(storeImageVO)); // storeImageVO 객체의 변경된 이름을 추가
	         }
	         
	         // 파일 정보 중 이미지 파일인 경우 섬네일 이름을 생성하여 savedList에 추가 
	         for(StoreImageVO storeImageVO : storeImageList) {
	            savedList.add(toThumbnailName(storeImageVO)); // storeImageVO 객체의 변경된 이름을 추가
	         }
	         log.warn(savedList);


			// 현재 날짜에서 1일 전 업로드 폴더 경로 생성
			File targetDir = Paths.get(uploadStoreImgPath, storeImageList.get(0).getStoreImagePath()).toFile();
			
			// 업로드 폴더에 저장된 파일 목록 중 
			// savedList에 파일 이름이 없는 경우만 조회
			File[] removeFiles = targetDir.listFiles(file -> savedList.contains(file.getName()) == false);

			for (File file : removeFiles) {
				log.warn(file.getAbsolutePath());
				file.delete(); // 파일 삭제
				log.info("이미지 파일 삭제");
			}

		}

		// 파일 이름 리턴
		private String toChgName(StoreImageVO storeImageVO) {
			return storeImageVO.getStoreImageChgName();
		}

		// 파일 이름을 썸네일 파일 이름으로 변경
		private String toThumbnailName(StoreImageVO storeImageVO) {
			return "t_" + storeImageVO.getStoreImageChgName() + 
					"." + storeImageVO.getStoreImageExtension();
		}

}

