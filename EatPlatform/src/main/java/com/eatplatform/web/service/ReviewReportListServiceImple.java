package com.eatplatform.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.domain.ReviewReportListVO;
import com.eatplatform.web.persistence.ReviewMapper;
import com.eatplatform.web.persistence.ReviewReportListMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReviewReportListServiceImple implements ReviewReportListService {

	@Autowired
	private ReviewReportListMapper reviewReportListMapper;

	@Autowired
	private ReviewMapper reviewMapper;
	
	@Transactional(value = "transactionManager")
	@Override
	public int createReviewReportList(ReviewReportListVO reviewReportListVO) {
		log.info("createReviewReportList()");
		
		int result = reviewReportListMapper.insert(reviewReportListVO);
		log.info(result + "행 신고 등록");
		
		// 신고 카운트
		int updateReport = reviewMapper
				.updateReportCount(reviewReportListVO.getReviewId(), 1);
		log.info(updateReport);
		
		return 1;
	}

	// 신고여부 확인
	@Override
	public int isReviewReported(int reviewId, int userId) {
		log.info("isReviewReported()");
		
		ReviewReportListVO reviewReportListVO = new ReviewReportListVO();
		reviewReportListVO.setReviewId(reviewId);
		reviewReportListVO.setUserId(userId);
		
		int result = reviewReportListMapper.checkReport(reviewReportListVO);
		if(result > 0) {
			log.info("이미 신고된 리뷰입니다.");
		}
		return result;
	}
	
}
