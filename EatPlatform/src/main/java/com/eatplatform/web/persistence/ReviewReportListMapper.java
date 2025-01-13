package com.eatplatform.web.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.ReviewReportListVO;

@Mapper
public interface ReviewReportListMapper {
	
	int insert(ReviewReportListVO reviewReportListVO);
	
	// 신고여부 확인
	int checkReport(ReviewReportListVO reviewReportListVO);
	
}
