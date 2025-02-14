package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.ReviewVO;

@Mapper
public interface ReviewMapper {
	List<ReviewVO> selectListByStoreId(int storeId);
	ReviewVO selectByReviewId(int reviewId); // reviewId 조회
	int selectLastReviewId(); // 이미지 첨부 시 reviewId 반환
	int insert(ReviewVO reviewVO);
	int update(ReviewVO reviewVO);
	int delete(int reviewId);
	
	// 추천 수 변경
	int updateLikeCount(@Param("reviewId") int reviewId, 
			@Param("amount") int amount);
	// 신고 수 변경
	int updateReportCount(@Param("reviewId") int reviewId,
			@Param("amount") int amount);
	
	// 페이징
	int countReviewsByStoreId(int storeId);
	List<ReviewVO> getReviewsByStoreId(@Param("storeId") int storeId, 
			@Param("start") int start, @Param("end") int end);
		
	// username 조회
	ReviewVO findReviewWithUsername(int reviewId);
	
}
