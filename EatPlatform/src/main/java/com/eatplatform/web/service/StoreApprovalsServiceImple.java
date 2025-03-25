package com.eatplatform.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.domain.JoinBusinessRequestVO;
import com.eatplatform.web.domain.JoinStoreApprovalsInfoVO;
import com.eatplatform.web.domain.JoinStoreApprovalsListVO;
import com.eatplatform.web.domain.JoinStoreWithStoreApprovalsVO;
import com.eatplatform.web.domain.StoreApprovalsVO;
import com.eatplatform.web.domain.StoreVO;
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
	
	@Autowired
	private NotificationService notificationService;

	// 가게 등록 요청 총 건수
	@Override
	public int getTotalCount() {
		log.info("getTotalCount()");
		return storeApprovalsMapper.selectTotalCount();
	}
	
	// 가게 등록 요청 목록 조회
	@Override
	public List<JoinStoreApprovalsListVO> searchList(Pagination pagination) {
		log.info("searchList()");
		List<JoinStoreApprovalsListVO> list = storeApprovalsMapper.selectApprovalsList(pagination);
		log.info("list : " + list);
		return list;
	}
	
	@Override
	public List<JoinStoreWithStoreApprovalsVO> getStoreApprovals(Pagination pagination, String username) {
		return storeApprovalsMapper.joinStoreWithStoreApprovalsByStoreUserId(pagination, username);
	}

	// 가게 등록 요청 정보
	@Override
	public JoinStoreApprovalsInfoVO searchInfo(int storeId) {
		log.info("searchInfo");
		return storeApprovalsMapper.selectApprovalInfoByStoreId(storeId);
	}

	// 가게 등록 요청 승인
	@Transactional(value = "transactionManager")
	@Override
	public int storeApproval(StoreApprovalsVO storeApprovalsVO) {
		log.info("storeApproval()");
		storeApprovalsVO.setApprovals(1);
		int result = storeApprovalsMapper.updateStoreApprovals(storeApprovalsVO);
		log.info("가게 등록 요청 " + result + "행 수정");
		
		// 가게 등록 승인 알림 전송
		notificationService.storeApprovalNotification(storeApprovalsVO.getStoreId());
		
		return result;
	}

	// 가게 등록 요청 거부, 취소
	@Transactional(value = "transactionManager")
	@Override
	public int denialManagement(int storeId) {
		log.info("denialManagement()");
		int approvals = 2;
		StoreApprovalsVO vo = new StoreApprovalsVO();
		vo.setStoreId(storeId);
		vo.setApprovals(approvals);
		int result = storeApprovalsMapper.updateStoreApprovals(vo);
		
		if(result == 1) {
			// 가게 등록 거부 알림 전송
			notificationService.storeApprovalNotification(storeId);
		}
		return result;
	}

	/**
	 * storeId로 StoreApprovalsVO 조회
	 */
	@Override
	public StoreApprovalsVO getApprovalsByStoreId(int storeId) {
		return storeApprovalsMapper.selectApprovalsByStoreId(storeId);
	}

	
}
