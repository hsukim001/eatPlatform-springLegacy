package com.eatplatform.web.persistence;

import java.util.List;

import com.eatplatform.web.domain.ReviewVO;

public interface ReviewMapper {
	int insert(ReviewVO reviewVO);
	List<ReviewVO> selectListByStoreId(int storeId);
	int update(ReviewVO reviewVO);
	int delete(int reviewId);
}
