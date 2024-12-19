package com.eatplatform.web.persistence;

import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.ReviewReportListVO;

public interface ReviewReportListMapper {
	
	// 중복 체크
	int checkReport(@Param("reviewId") int reviweId, 
			@Param("userId") String userId);
	
	int insert(ReviewReportListVO reviewReportListVO);
	
}
