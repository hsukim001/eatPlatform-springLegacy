package com.eatplatform.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.domain.BusinessRequestVO;
import com.eatplatform.web.domain.JoinBusinessRequestVO;
import com.eatplatform.web.domain.JoinBusinessRequestWithUserAndRoleListVO;
import com.eatplatform.web.domain.JoinUserVO;
import com.eatplatform.web.domain.RoleListVO;
import com.eatplatform.web.persistence.BusinessRequestMapper;
import com.eatplatform.web.persistence.UserMapper;
import com.eatplatform.web.persistence.RoleListMapper;
import com.eatplatform.web.persistence.UserStoreMapper;
import com.eatplatform.web.util.Pagination;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class BusinessRequestServiceImple implements BusinessRequestService {

	@Autowired
	private BusinessRequestMapper businessRequestMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserStoreMapper userStoreMapper;

	@Autowired
	private RoleListMapper roleListMapper;
	
	@Autowired
	private NotificationService notificationService;

	// 사업자 등록 신청
	@Override
	public int businessRequest(int userId) {
		return businessRequestMapper.insertBusinessRequest(userId);
	}

	// 사업자 등록 신청 정보 조회
	@Override
	public JoinBusinessRequestVO searchBusinessRequestInfo(int businessRequestId) {
		log.info("searchBusinessRequestInfo()");
		JoinBusinessRequestVO vo = businessRequestMapper.selectBusinessRequestByBusinessRequestId(businessRequestId);
		return vo;
	}

	// 사업자 등록 신청 목록
	@Override
	public List<JoinBusinessRequestVO> searchBusinessRequestList(Pagination pagination) {
		log.info("searchBusinessRequestList()");
		List<JoinBusinessRequestVO> list = businessRequestMapper.selectBusinessRequestListByPagination(pagination);
		return list;
	}

	// 사업자 등록 신청 목록 총 건수
	@Override
	public int getBusinessRequestTotalCount() {
		log.info("getBusinessRequestTotalCount()");
		return businessRequestMapper.selectTotalCount();
	}

	// 사업자 등록 신청 조회(userId)
	@Override
	public int getBusinessRequestId(int userId) {
		log.info("getBusinessRequestId()");
		return businessRequestMapper.selectBusinessRequestIdByuserId(userId);
	}

	// 사업자 등록 요청 승인
	@Transactional
	@Override
	public int businessRequestApploval(int businessRequestId) {
		log.info("businessReqeustApprovals()");
		int result = 0;
		JoinBusinessRequestWithUserAndRoleListVO joinBusinessRequestWithUserAndRoleListVO = businessRequestMapper.joinBusinessRequestWithUserMemberAndRoleListByBusinessRequestId(businessRequestId);
		int userId = joinBusinessRequestWithUserAndRoleListVO.getUserId();
		String requestStatus = "APPROVAL";
		int updateRequestStatusResult = businessRequestMapper.updateRequestStatusByBusinessRequestId(businessRequestId, requestStatus);
		
		// 사업자 등록 요청 결과 알림 전송
		notificationService.businessRequestNotification(businessRequestId);
		
		// 권한 변경 로직
		RoleListVO roleListVO = new RoleListVO();
		roleListVO.setRoleId(joinBusinessRequestWithUserAndRoleListVO.getRoleId());
		roleListVO.setUsername(joinBusinessRequestWithUserAndRoleListVO.getUsername());
		roleListVO.setRoleName("ROLE_STORE");
		int updateRoleListResult = roleListMapper.updateUserRoleNameByUserName(roleListVO);

		// 회원 테이블 변경 로직
		int insertUserStoreFromUserResult = userStoreMapper.insertUserStoreFromUser(userId);
		int deleteUserResult = userMapper.deleteUserByUserId(userId);
		
		if(updateRoleListResult == 1 && insertUserStoreFromUserResult == 1 && deleteUserResult == 1 && updateRequestStatusResult == 1) {
			result = 1;
		}

		return result;
	}

	// 사업자 등록 요청 수정
	@Override
	public int updateRequestStatus(int businessRequestId, String requestStatus) {
		return businessRequestMapper.updateRequestStatusByBusinessRequestId(businessRequestId, requestStatus);
	}

	// 사업자 등록 요청 취소
	@Override
	public int businessRequestCancel(int businessRequestId) {
		return businessRequestMapper.deleteBusinessRequest(businessRequestId);
	}

	@Override
	public boolean isBusinessRequestRoleMemberAndRequestStatusWait(int businessRequestId) {
		JoinBusinessRequestWithUserAndRoleListVO vo = businessRequestMapper.joinBusinessRequestWithUserMemberAndRoleListByBusinessRequestId(businessRequestId);
		if(vo.getRoleName().equals("ROLE_MEMBER")) {
			
			// 사업자 등록 요청 결과 알림 전송
			notificationService.businessRequestNotification(businessRequestId);
			
			return true;
		}
		return false;
	}
}
