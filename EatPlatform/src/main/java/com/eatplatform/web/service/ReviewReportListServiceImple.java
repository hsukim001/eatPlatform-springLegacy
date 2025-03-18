package com.eatplatform.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.domain.JoinReviewReportVO;
import com.eatplatform.web.domain.ReviewReportListVO;
import com.eatplatform.web.domain.ReviewReportListWithUserAtNameVO;
import com.eatplatform.web.persistence.ReviewMapper;
import com.eatplatform.web.persistence.ReviewReportListMapper;
import com.eatplatform.web.util.Pagination;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReviewReportListServiceImple implements ReviewReportListService {

	@Autowired
	private ReviewReportListMapper reviewReportListMapper;

	@Autowired
	private ReviewMapper reviewMapper;
	
	@Override
	public List<JoinReviewReportVO> getReviewReportList(Pagination pagination) {
		return reviewMapper.selectReviewReportByPagination(pagination);
	}
	
	@Override
	public List<ReviewReportListVO> getReviewReportListByReviewId(int reviewId) {
		return reviewReportListMapper.selectReportListByReviewId(reviewId);
	}
	
	@Override
	public JoinReviewReportVO getReviewReportInfo(int reviewId) {
		return reviewMapper.selectReviewReportByReviewId(reviewId);
	}
	
	@Override
	public int getTotalCount() {
		return reviewReportListMapper.getTotalCount();
	}
	
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

	@Transactional
	@Override
	public int deleteReview(int reviewId) {
		int result = 0;
		int reviewResult = reviewMapper.delete(reviewId);
		int reportResult = reviewReportListMapper.deleteReviewReportByReviewId(reviewId);
		if(reviewResult == 1 && reportResult == 1) {
			result = 1;
		}
		return result;
	}
	
}
