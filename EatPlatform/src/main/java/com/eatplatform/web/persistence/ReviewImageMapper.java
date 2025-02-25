package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.ReviewImageVO;

@Mapper
public interface ReviewImageMapper {
	
	int insertReviewImage(ReviewImageVO reviewImageVO);
	
	ReviewImageVO selectByReviewImageId(int reviewImageId);
	
	List<ReviewImageVO> selectListByReviewId(int reviewId);
	
	int deleteReviewImageByReviewId(int reviewId);
	
	/**
	 * 이미지 파일 삭제
	 * @param reviewImageDate
	 * @return
	 */
	List<ReviewImageVO> selectListByReviewImagePath(
			@Param("reviewImageDate") String reviewImageDate);
	
}
