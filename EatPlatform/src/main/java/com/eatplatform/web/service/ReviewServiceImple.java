package com.eatplatform.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.domain.ReviewImageVO;
import com.eatplatform.web.domain.ReviewVO;
import com.eatplatform.web.persistence.ReplyMapper;
import com.eatplatform.web.persistence.ReviewImageMapper;
import com.eatplatform.web.persistence.ReviewLikeListMapper;
import com.eatplatform.web.persistence.ReviewMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReviewServiceImple implements ReviewService{

	@Autowired
	private ReviewMapper reviewMapper;
	
	@Autowired
	private ReplyMapper replyMapper;
	
	@Autowired
	private ReviewLikeListMapper reviewLikeListMapper;
	
	@Autowired ReviewImageMapper reviewImageMapper;
	
	@Transactional(value = "transactionManager")
	@Override
	public int createReview(ReviewVO reviewVO) {
		log.info("createReview()");
		int result = reviewMapper.insert(reviewVO);
		log.info(result + "행 리뷰 등록");
		
		// 리뷰 이미지 첨부
		List<ReviewImageVO> reviewImageList = reviewVO.getreviewImageList();

		for(ReviewImageVO reviewImageVO : reviewImageList) {
			reviewImageVO.setReviewId(reviewMapper.selectLastReviewId());
			int imageResult = reviewImageMapper.insertReviewImage(reviewImageVO);
			log.info("이미지 등록 : " + reviewImageList);
			
		}
		return 1;
	}

	@Override
	public List<ReviewVO> getAllReview(int storeId) {
		log.info("getAllReview()");
		return reviewMapper.selectListByStoreId(storeId);
		
	}

	@Override
	public int updateReview(ReviewVO reviewVO) {
		log.info("updateReview()");
		int result = reviewMapper.update(reviewVO);
		log.info(result + "행 리뷰 수정");
		return 1;
	}
	
	@Transactional(value = "transactionManager")
	@Override
	public int deleteReview(int reviewId) {
		log.info("deleteReview()");
		int result = reviewMapper.delete(reviewId);
		log.info(result + "행 리뷰 삭제");
		
		// 리뷰 댓글 삭제
		int deleteReply = replyMapper.deleteByReviewId(reviewId);
		log.info(deleteReply + "댓글 삭제");
		
		// 추천인 목록 삭제
		int deleteReviewLikeList = reviewLikeListMapper.delete(reviewId);
		log.info(deleteReviewLikeList + "추천 목록 삭제");
		
		// 첨부된 이미지 데이터 삭제
		int deleteReviewImage = reviewImageMapper.deleteReviewImageByReviewId(reviewId);
		log.info(deleteReviewImage + "이미지 데이터 삭제");
		
		return 1;
		
	}

	@Override
	public int countAllReviewsByStoreId(int storeId) {
		log.info("countAllReviewsByStoreId()");
		int result = reviewMapper.countReviewsByStoreId(storeId);
		log.info(result + "개");
		return result;
	}

	@Override
	public List<ReviewVO> getAllReviewsByStoreId(int storeId, int start, int end) {
		log.info("getAllReviewsByStoreId()");
		return reviewMapper.getReviewsByStoreId(storeId, start, end);
	}

}
