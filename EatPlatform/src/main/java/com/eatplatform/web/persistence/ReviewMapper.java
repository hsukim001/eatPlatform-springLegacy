package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.ReviewVO;
import com.eatplatform.web.util.Pagination;

@Mapper
public interface ReviewMapper {
	int insert(ReviewVO reviewVO);
	List<ReviewVO> selectListByStoreId(int storeId);
	int update(ReviewVO reviewVO);
	int delete(int reviewId);
	
	// 추천 수 변경
	int updateLikeCount(@Param("reviewId") int reviewId, 
			@Param("amount") int amount);
	// 신고 수 변경
	int updateReportCount(@Param("reviewId") int reviewId,
			@Param("amount") int amount);
	
	// 리뷰 페이징
	List<ReviewVO> selectListByPagination(Pagination pagination);
	int selectTotalCount();
	
}
