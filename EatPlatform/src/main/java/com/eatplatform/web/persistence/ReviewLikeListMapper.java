package com.eatplatform.web.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewLikeListMapper {
	
	/**
	 * 추천 리스트 등록
	 * @param reviewId
	 * @param userId
	 * @return
	 */
	int insert(@Param("reviewId") int reviewId, 
			@Param("userId") int userId);
	
	int delete(int reviewId);
	 
	/**
	 * 추천 리스트 중복 체크
	 * @param reviewId
	 * @param userId
	 * @return
	 */
	int checkLike(@Param("reviewId") int reviewId, 
			@Param("userId") int userId);
}
