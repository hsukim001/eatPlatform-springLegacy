package com.eatplatform.web.persistence;

import java.util.List;

import com.eatplatform.web.domain.ReviewImageVO;

public interface ReviewImageMapper {
	int insert(ReviewImageVO reviewImageVO);
	ReviewImageVO selectByReviewImageId(int reviewImageId);
	List<ReviewImageVO> selectReviewImageIdList(); // 파일 하나씩 확인
	int update(ReviewImageVO reviewImageVO);
	int delete(int reviewImageId);
}
