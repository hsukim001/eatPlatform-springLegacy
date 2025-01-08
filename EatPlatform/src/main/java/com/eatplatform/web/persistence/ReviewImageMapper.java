package com.eatplatform.web.persistence;

import java.util.List;

import com.eatplatform.web.domain.ReviewImageVO;

public interface ReviewImageMapper {
	
	ReviewImageVO selectByReviewImageId(int reviewImageId);
	List<ReviewImageVO> selectListByReviewId(int reviewId);
	
}
