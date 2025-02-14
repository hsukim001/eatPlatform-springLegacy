package com.eatplatform.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.persistence.ReviewLikeListMapper;
import com.eatplatform.web.persistence.ReviewMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReviewLikeListServiceImple implements ReviewLikeListService{

	@Autowired
	private ReviewLikeListMapper reviewLikeListMapper;
	
	@Autowired
	private ReviewMapper reviewMapper;
	
	@Transactional(value = "transactionManager")
	@Override
	public int createReviewLikeList(int reviewId, int userId) {
		log.info("createReviewLikeList()");
		
		// 추천여부 확인
		int checkResult = reviewLikeListMapper.checkLike(reviewId, userId);
		if(checkResult > 0) {
			log.info("이미 추천된 리뷰입니다.");
			return 0;
		}
		
		int result = reviewLikeListMapper.insert(reviewId, userId);
		log.info(result + "행 추천 등록");
		
		// 추천 카운트
		int updateLike = reviewMapper.updateLikeCount(reviewId, 1);
		log.info(updateLike);
		
		return 1;
	}

}
