package com.eatplatform.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.ReviewImageVO;
import com.eatplatform.web.persistence.ReviewImageMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReviewImageServiceImple implements ReviewImageService{
	
	@Autowired
	private ReviewImageMapper reviewImageMapper;

	@Override
	public int createReviewImage(ReviewImageVO reviewImageVO) {
		log.info("createReviewImage()");
		return reviewImageMapper.insert(reviewImageVO);
	}

	@Override
	public ReviewImageVO getReviewImageById(int reviewImageId) {
		log.info("getReviewImageById()");
		return reviewImageMapper.selectByReviewImageId(reviewImageId);
	}

	@Override
	public List<ReviewImageVO> getAllImageId() {
		log.info("getAllImageId()");
		return reviewImageMapper.selectReviewImageIdList();
	}

	@Override
	public int updateReviewImage(ReviewImageVO reviewImageVO) {
		log.info("updateReviewImage()");
		return reviewImageMapper.update(reviewImageVO);
	}

	@Override
	public int deleteReviewImage(int reviewImageId) {
		log.info("deleteReviewImage()");
		return reviewImageMapper.delete(reviewImageId);
	}

}