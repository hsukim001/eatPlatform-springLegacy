package com.eatplatform.web.persistence;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.JoinReviewReportVO;
import com.eatplatform.web.domain.ReviewVO;
import com.eatplatform.web.util.Pagination;

@Mapper
public interface ReviewMapper {
	int insert(ReviewVO reviewVO);
	int selectLastReviewId(); // 이미지 첨부 시 reviewId 반환
	List<ReviewVO> selectListByStoreId(int storeId);
	ReviewVO selectByReviewId(int reviewId); // reviewId 조회
	int update(ReviewVO reviewVO);
	int delete(int reviewId);
	BigDecimal selectAverageStarByStoreId(Long storeId);
	
	/**
	 * 추천 수 변경
	 * @param reviewId
	 * @param amount
	 * @return
	 */
	int updateLikeCount(@Param("reviewId") int reviewId, 
			@Param("amount") int amount);

	/**
	 * 신고 수 변경
	 * @param reviewId
	 * @param amount
	 * @return
	 */
	int updateReportCount(@Param("reviewId") int reviewId,
			@Param("amount") int amount);
	
	/**
	 * 페이징
	 * @param storeId
	 * @param start
	 * @param end
	 * @return
	 */
	List<ReviewVO> getReviewsByStoreId(@Param("storeId") int storeId, 
			@Param("start") int start, @Param("end") int end);
	int countReviewsByStoreId(int storeId); // 리뷰 개수
		
	// username 조회
	ReviewVO findReviewWithUsername(int reviewId);
	
	/**
	 * @param Pagination pagination
	 * @return List<JoinReviewReportVO>
	 */
	List<JoinReviewReportVO> selectReviewReportByPagination(Pagination pagination);
	
	/**
	 * @param int reviewId
	 * @return JoinReviewReportVO
	 */
	JoinReviewReportVO selectReviewReportByReviewId(int reviewId);
	
	/**
	 * @return int
	 */
	int getReviewReportCount();
	
}
