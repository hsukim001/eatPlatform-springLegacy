package com.eatplatform.web.persistence;

import org.apache.ibatis.annotations.Param;

public interface ReviewLikeListMapper {
	
	// 중복 체크
	int checkLike(@Param("reviewId") int reviewId, @Param("userId") String userId);
	
	int insert(@Param("reviewId") int reviewId, @Param("userId") String userId);
}
