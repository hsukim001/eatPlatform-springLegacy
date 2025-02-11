package com.eatplatform.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.JoinBusinessRequestVO;
import com.eatplatform.web.domain.StoreApprovalsVO;
import com.eatplatform.web.persistence.StoreApprovalsMapper;
import com.eatplatform.web.persistence.StoreMapper;
import com.eatplatform.web.util.Pagination;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class StoreApprovalsServiceImple implements StoreApprovalsService{
	
	@Autowired
	private StoreApprovalsMapper storeApprovalsMapper;
	
	@Autowired
	private StoreMapper storeMapper;

	// 가게 등록 요청 총 건수
	@Override
	public int getTotalCount() {
		log.info("getTotalCount()");
		return storeApprovalsMapper.selectTotalCount();
	}
	
	// 가게 등록 요청 목록 조회
	@Override
	public List<JoinBusinessRequestVO> searchList(Pagination pagination) {
		log.info("searchList()");
		List<JoinBusinessRequestVO> list = storeApprovalsMapper.selectApprovalsList(pagination);
		log.info("list : " + list);
		return list;
	}

	// 가게 등록 요청 정보
	@Override
	public JoinBusinessRequestVO searchInfo(int storeId) {
		log.info("searchInfo");
		return storeApprovalsMapper.selectApprovals(storeId);
	}

	// 가게 등록 요청 승인
	@Override
	public int storeApproval(StoreApprovalsVO storeApprovalsVO) {
		log.info("storeApproval()");
		storeApprovalsVO.setApprovals(1);
		int result = storeApprovalsMapper.updateStoreApprovals(storeApprovalsVO);
		log.info("가게 등록 요청 " + result + "행 수정");
		return result;
	}

	// 가게 등록 요청 거부, 취소
	@Override
	public int denialManagement(int storeId) {
		log.info("denialManagement()");
		return storeMapper.deleteStore(storeId);
	}

	
}
