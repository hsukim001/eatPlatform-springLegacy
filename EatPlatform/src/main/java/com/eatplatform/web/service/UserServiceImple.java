package com.eatplatform.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.domain.BusinessRequestVO;
import com.eatplatform.web.domain.RequestInfoVO;
import com.eatplatform.web.domain.UserRoleVO;
import com.eatplatform.web.domain.StoreAddressVO;
import com.eatplatform.web.domain.StoreApprovalsVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.persistence.BusinessRequestMapper;
import com.eatplatform.web.persistence.StoreAddressMapper;
import com.eatplatform.web.persistence.StoreApprovalsMapper;
import com.eatplatform.web.persistence.StoreMapper;
import com.eatplatform.web.persistence.UserAdminMapper;
import com.eatplatform.web.persistence.UserMapper;
import com.eatplatform.web.persistence.UserRoleMapper;
import com.eatplatform.web.persistence.UserStoreMapper;
import com.eatplatform.web.util.Pagination;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class UserServiceImple implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserStoreMapper userStoreMapper;

	@Autowired
	private UserAdminMapper userAdminMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private BusinessRequestMapper businessRequestMapper;

	@Autowired
	private StoreMapper storeMapper;

	@Autowired
	private StoreAddressMapper storeAddressMapper;

	@Autowired
	private StoreApprovalsMapper storeApprovalsMapper;

	// 회원 정보 검색(userId)
	@Override
	public UserVO searchUser(int userId) {
		log.info("searchUser()");
		return userMapper.selectUserByUserId(userId);
	}

	// 아이디 중복 확인(userId)
	@Override
	public int checkUser(String username) {
		log.info("checkUser()");
		int count = userMapper.countUser(username);
		log.info("count : " + count);
		return count;
	}

	// 회원 가입
	@Transactional
	@Override
	public int createdUser(UserVO userMemberVO) {
		log.info("createdUser()");
		int resultUser = userMapper.insertUser(userMemberVO);

		UserRoleVO roleListVO = new UserRoleVO();
		roleListVO.setUsername(userMemberVO.getUsername());
		roleListVO.setRoleName("ROLE_MEMBER");
		int resultRole = userRoleMapper.insertUserRole(roleListVO);

		log.info("회원 정보 " + resultUser + "행 등록 성공");
		log.info("회원 권한 " + resultRole + "행 등록 성공");
		return 1;
	}
	
	// 회원 아이디 찾기(email)
	@Override
	public String searchUsername(String email) {
		UserVO vo = userMapper.selectUsernameByEmail(email);
		String username = vo.getUsername();
		int index = username.length() - 2;
		log.info(index);
		String chUsername = username.substring(0, 2);
		for(int i = 0; i < index; i++) {
			chUsername += "*";
		}
		return chUsername;
	}

	// 회원 정보 수정
	@Override
	public int updateUser(UserVO userMemberVO) {
		return userMapper.updateUser(userMemberVO);
	}

	// 비밀번호 수정
	@Override
	public int modifyPassword(UserVO userMemberVO, String auth) {
		log.info("modifyPassword()");
		log.info(userMemberVO);
		log.info("auth : " + auth);
		int result = 0;

		if (!auth.isEmpty()) { // 로그인 수정
			if (auth.contains("ROLE_MEMBER")) {
				log.info("권한 : MEMBER");
				result = userMapper.updatePassword(userMemberVO);
			} else if (auth.contains("ROLE_STORE")) {
				log.info("권한 : STORE");
				result = userStoreMapper.updatePassword(userMemberVO);
			} else if (auth.contains("ROLE_ADMIN")) {
				log.info("권한 : ADMIN");
				result = userAdminMapper.updatePassword(userMemberVO);
			}
		} else { // 비 로그인 수정
			UserVO vo = userMapper.selectUserAllByEmail(userMemberVO.getEmail());
			UserRoleVO role = userRoleMapper.selectUserRoleByUsername(vo.getUsername());
			if (role.getRoleName().equals("ROLE_MEMBER")) {
				log.info("권한 : MEMBER");
				result = userMapper.updatePassword(userMemberVO);
			} else if (role.getRoleName().equals("ROLE_STORE")) {
				log.info("권한 : STORE");
				result = userStoreMapper.updatePassword(userMemberVO);
			} else if (role.getRoleName().equals("ROLE_ADMIN")) {
				log.info("권한 : ADMIN");
				result = userAdminMapper.updatePassword(userMemberVO);
			}
		}

		return result;
	}
	
	// 회원 탈퇴
	@Override
	public int withdrawalUser(int userId, String auth) {
		log.info("withdrawalUser()");
		
		int result = 0;
		UserVO user = new UserVO();
		user.setUserId(userId);
		user.setActive(0);
		
		if(auth.contains("ROLE_MEMBER")) {
			result = userMapper.updateUserActive(user);
		} else if(auth.contains("ROLE_STORE")) {
			result = userStoreMapper.updateUserActive(user);
		} else if(auth.contains("ROLE_ADMIN")) {
			result = userAdminMapper.updateUserActive(user);
		}
		
		return result;
	}
	
	// 탈퇴 회원 정보 탈퇴 목록으로 이전
	@Transactional
	@Override
	public int transferWithdrawalUser(int active) {
		log.info("transferWithdrawalUser()");
		
		int withdrwalMemberResult = userMapper.insertWithdrawlUserByActive(active);
		int withdrwalStoreResult = userStoreMapper.insertWithdrawlUserByActive(active);
		int withdrwalAdminResult = userAdminMapper.insertWithdrawlUserByActive(active);
		log.info("사용자 정보 " + withdrwalMemberResult + "행 withdrawal 테이블 저장");
		log.info("사업자 정보 " + withdrwalStoreResult + "행 withdrawal 테이블 저장");
		log.info("관리자 정보 " + withdrwalAdminResult + "행 withdrawal 테이블 저장");
		
		if(withdrwalMemberResult >= 1) {
			int deleteWithdrowalUserRoleResult = userRoleMapper.deleteWithdrowalUserRole();
			log.info("권한 정보 " + deleteWithdrowalUserRoleResult + "행 삭제");
		} else if(withdrwalStoreResult >= 1) {
			int deleteWithdrowalStoreUserRoleResult = userRoleMapper.deleteWithdrowalStoreUserRole();
			log.info("권한 정보 " + deleteWithdrowalStoreUserRoleResult + "행 삭제");
		} else if(withdrwalAdminResult >= 1) {
			int deleteWithdrowalAdminUserRoleResult = userRoleMapper.deleteWithdrowalAdminUserRole();
			log.info("권한 정보 " + deleteWithdrowalAdminUserRoleResult + "행 삭제");
		}
		
		int deleteWithdrowalUserMemberResult = userMapper.deleteUserByActive(active);
		int deleteWithdrowalUserStoreResult = userStoreMapper.deleteUserByActive(active);
		int deleteWithdrowalUserAdminResult = userAdminMapper.deleteUserByActive(active);
				
		log.info("사용자 정보 " + deleteWithdrowalUserMemberResult + "행 삭제");
		log.info("사업자 정보 " + deleteWithdrowalUserStoreResult + "행 삭제");
		log.info("관리자 정보 " + deleteWithdrowalUserAdminResult + "행 삭제");
		
		return 1;
	}

	// 사업자 등록 신청
	@Override
	public int businessRequest(StoreVO storeVO, StoreAddressVO storeAddressVO) {
		log.info("businessUpgradeForm()");

		int insertStore = storeMapper.insertStore(storeVO);
		int storeId = storeVO.getStoreId();
		String username = storeVO.getUsername();
		log.info("storeId : " + storeId);

		storeAddressVO.setStoreId(storeId);
		int insertStoreAddress = storeAddressMapper.insertStoreAddress(storeAddressVO);

		BusinessRequestVO businessRequestVO = new BusinessRequestVO();
		businessRequestVO.setStoreId(storeId);
		businessRequestVO.setUsername(username);
		businessRequestVO.setRequestApprovals(0);
		int insertBusinessRequest = businessRequestMapper.insertBusinessRequest(businessRequestVO);

		StoreApprovalsVO storeApprovalsVO = new StoreApprovalsVO();
		storeApprovalsVO.setStoreId(storeId);
		storeApprovalsVO.setApprovals(0);
		int insertStoreApprovals = storeApprovalsMapper.insertStoreApprovals(storeApprovalsVO);

		log.info("Store : " + insertStore + "행 등록 성공");
		log.info("StoreAddress : " + insertStoreAddress + "행 등록 성공");
		log.info("businessRequest : " + insertBusinessRequest + "행 등록 성공");
		log.info("StoreApprovals : " + insertStoreApprovals + "행 등록 성공");

		return 1;
	}

	// 사업자 등록 신청 정보 조회
	@Override
	public RequestInfoVO searchBusinessRequestInfo(int businessRequestId) {
		log.info("searchBusinessRequestInfo()");
		RequestInfoVO vo = businessRequestMapper.selectBusinessRequestByBusinessRequestId(businessRequestId);
		log.info(vo);
		return vo;
	}

	// 사업자 등록 신청 목록
	@Override
	public List<RequestInfoVO> searchBusinessRequestList(Pagination pagination) {
		log.info("searchBusinessRequestList()");
		return businessRequestMapper.selectBusinessRequestListByPagination(pagination);
	}

	// 사업자 등록 신청 목록 총 건수
	@Override
	public int getBusinessRequestTotalCount() {
		log.info("getBusinessRequestTotalCount()");
		return businessRequestMapper.selectTotalCount();
	}

	// 사업자 등록 신청 조회(userId)
	@Override
	public int getBusinessRequestId(String username) {
		log.info("getBusinessRequestId()");
		return businessRequestMapper.selectBusinessRequestIdByuserId(username);
	}

	// 사업자 등록 요청 승인
	@Transactional(value = "transactionManager")
	@Override
	public int businessReqeustApprovals(int businessRequestId, int storeId) {
		log.info("businessReqeustApprovals()");

		BusinessRequestVO businessRequestVO = businessRequestMapper.selectBusinessRequest(businessRequestId);
		String username = businessRequestVO.getUsername();

		int deleteBusinessRequest = businessRequestMapper.deleteBusinessRequest(businessRequestId);

		StoreApprovalsVO storeApprovalsVO = new StoreApprovalsVO();
		storeApprovalsVO.setStoreId(storeId);
		storeApprovalsVO.setApprovals(1);
		int updateStoreApprovals = storeApprovalsMapper.updateStoreApprovals(storeApprovalsVO);

		UserRoleVO userRoleVO = new UserRoleVO();
		userRoleVO.setUsername(username);
		userRoleVO.setRoleName("ROLE_STORE");
		int updateUserRole = userRoleMapper.updateUserRoleNameByUserName(username);

		log.info("사업자 등록 요청 : " + deleteBusinessRequest + "행 삭제");
		log.info("식당 등록 요청 정보 : " + updateStoreApprovals + "행 수정");
		log.info("회원 권한 : " + updateUserRole + "행 수정");

		return 1;
	}

	// 사업자 등록 요청 거부
	@Transactional(value = "transactionManager")
	@Override
	public int businessReqeustDenialManagement(int businessRequestId, int storeId) {
		log.info("businessReqeustDenied()");

		int deleteStore = storeMapper.deleteStore(storeId);
		int deleteBusinessRequest = businessRequestMapper.deleteBusinessRequest(businessRequestId);
		log.info("식당 : " + deleteStore + "행 삭제");
		log.info("사업자 등록 요청 : " + deleteBusinessRequest + "행 삭제");

		return 1;
	}

}
