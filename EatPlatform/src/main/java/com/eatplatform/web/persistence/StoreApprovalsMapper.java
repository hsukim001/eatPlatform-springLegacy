package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.JoinBusinessRequestVO;
import com.eatplatform.web.domain.JoinStoreApprovalsInfoVO;
import com.eatplatform.web.domain.JoinStoreApprovalsListVO;
import com.eatplatform.web.domain.JoinStoreWithStoreApprovalsVO;
import com.eatplatform.web.domain.StoreApprovalsVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.util.Pagination;

@Mapper
public interface StoreApprovalsMapper {
	// 가게 등록 요청 총 건수
	int selectTotalCount();
	// 가게 등록 요청
	int insertStoreApprovals(StoreApprovalsVO storeApprovalsVO);
	// 가게 등록 요청 목록
	List<JoinStoreApprovalsListVO> selectApprovalsList(Pagination pagination);
	
	/**
	 * @param String username
	 * @return List<JoinStoreWithStoreApprovalsVO>
	 */
	List<JoinStoreWithStoreApprovalsVO> joinStoreWithStoreApprovalsByStoreUserId(@Param("pagination") Pagination pagination, @Param("username") String username);
	// 가게 등록 요청 상세
	JoinStoreApprovalsInfoVO selectApprovalInfoByStoreId(int storeId);
	// 식당 등록 요청 승인
	int updateStoreApprovals(StoreApprovalsVO storeApprovalsVO);
	
	StoreApprovalsVO selectApprovalsByStoreId(int storeId);
}
