package com.eatplatform.web.task;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eatplatform.web.domain.ReviewImageVO;
import com.eatplatform.web.persistence.ReviewImageMapper;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class ReviewImageTask {
	
	@Autowired
	private String uploadPath;
	
	@Autowired
	private ReviewImageMapper reviewImageMapper;

	@Scheduled(cron = "0 00 10 * * *")  // 매일 10시에 실행
	
		public static String getFormattedDateString() {
		
			log.info("getFormattedDateString()");
			
			// 현재 날짜에서 1일 전 날짜를 구함
			LocalDate yesterday = LocalDate.now().minusDays(1);
			log.info("yesterday:" + yesterday);
			
			// 날짜 형식을 "yyyy/MM/dd" 형식으로 설정
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			
			return yesterday.format(formatter);
		
		}
	
	@Scheduled(cron = "0 00 10 * * *")  // 매일 10시에 실행
	
		public void deleteReviewImages() {
			log.warn("===========================");
			log.warn("Delete ReviewImage Task Run");
			
			// 1일전 날짜의 첨부 파일 경로에 해당하는 파일 정보 조회
			List<ReviewImageVO> reviewImageList = reviewImageMapper.selectListByReviewImagePath(getFormattedDateString().replace("/", "\\"));
			log.info("reviewImageList : " + reviewImageList);
			
			// 전날 이미지 데이터가 0개인데, 파일은 남아있을 때
			if (reviewImageList.size() == 0) {
				File targetDir = Paths.get(uploadPath, getFormattedDateString()).toFile();
				
				File[] removeFiles = targetDir.listFiles();
				
				for (File file : removeFiles) {
					log.warn(file.getAbsolutePath());
					file.delete(); // 파일 삭제
					log.info("이미지 파일 삭제");
				}
				return;
				
			}
			
			// 파일 정보에서 파일 이름만 추출하여 List<String>으로 변경
			List<String> savedList = reviewImageList.stream() // 데이터 처리 기능 사용을 위한 stream 변경
					.map(this::toChgName) // attach를 attach.getAttachChgName()으로 변경 
					.collect(Collectors.toList()); // stream을 list로 변경
			
			// 파일 정보 중 이미지 파일인 경우 섬네일 이름을 생성하여 savedList에 추가 
			reviewImageList.stream()
				.map(this::toThumbnailName) // 섬네일 이름으로 변경
				.forEach(name -> savedList.add(name)); // savedList에 각 섬네일 이름 저장
			log.warn(savedList);

			// 현재 날짜에서 1일 전 업로드 폴더 경로 생성
			File targetDir = Paths.get(uploadPath, reviewImageList.get(0).getReviewImagePath()).toFile();
			
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
		private String toChgName(ReviewImageVO reviewImageVO) {
			return reviewImageVO.getReviewImageChgName();
		}

		// 파일 이름을 썸네일 파일 이름으로 변경
		private String toThumbnailName(ReviewImageVO reviewImageVO) {
			return "t_" + reviewImageVO.getReviewImageChgName() + 
					"." + reviewImageVO.getReviewImageExtension();
		}

}

