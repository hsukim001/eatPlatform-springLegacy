package com.eatplatform.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.ReviewVO;
import com.eatplatform.web.persistence.ReviewMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReviewServiceImple implements ReviewService{

	@Autowired
	private ReviewMapper reviewMapper;
	
	@Override
	public int createReview(ReviewVO reviewVO) {
		log.info("createReview()");
		int result = reviewMapper.insert(reviewVO);
		log.info(result + "행 리뷰 등록");
		return 1;
	}

	@Override
	public List<ReviewVO> getAllReview(int storeId) {
		log.info("getAllReview()");
		return reviewMapper.selectListByStoreId(storeId);
	}

	@Override
	public int updateReview(ReviewVO reviewVo) {
		log.info("updateReview()");
		ReviewVO reviewVO = new ReviewVO();
		int result = reviewMapper.update(reviewVo);
		log.info(result + "행 리뷰 수정");
		return 1;
	}

	@Override
	public int deleteReview(int reviewId) {
		log.info("deleteReview()");
		int result = reviewMapper.delete(reviewId);
		log.info(result + "행 리뷰 삭제");
		return 1;
	}

}
