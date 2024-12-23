package com.eatplatform.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.domain.ReviewVO;
import com.eatplatform.web.persistence.ReplyMapper;
import com.eatplatform.web.persistence.ReviewMapper;
import com.eatplatform.web.util.PageMaker;
import com.eatplatform.web.util.Pagination;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReviewServiceImple implements ReviewService{

	@Autowired
	private ReviewMapper reviewMapper;
	
	@Autowired
	private ReplyMapper replyMapper;
	
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
		log.info(deleteReply + "행 댓글 삭제");
		return 1;
	}

	@Override
	public List<ReviewVO> getPagingReviews(Pagination pagination) {
		log.info("getPagingReviews()");
		return reviewMapper.selectListByPagination(pagination);
	}

	@Override
	public int getReviewCount() {
		log.info("getReviewCount()");
		return reviewMapper.selectTotalCount();
	}



}
