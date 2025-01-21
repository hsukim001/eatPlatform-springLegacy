package com.eatplatform.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.domain.BusinessRequestInfoVO;
import com.eatplatform.web.domain.BusinessRequestVO;
import com.eatplatform.web.domain.StoreAddressVO;
import com.eatplatform.web.domain.StoreApprovalsVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.persistence.BusinessRequestMapper;
import com.eatplatform.web.persistence.StoreAddressMapper;
import com.eatplatform.web.persistence.StoreApprovalsMapper;
import com.eatplatform.web.persistence.StoreMapper;
import com.eatplatform.web.persistence.UserDelMapper;
import com.eatplatform.web.persistence.UserMapper;
import com.eatplatform.web.util.Pagination;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class UserServiceImple implements UserService{
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserDelMapper userDelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private BusinessRequestMapper businessRequestMapper;
	
	@Autowired
	private StoreApprovalsMapper storeApprovalsMapper;
	
	@Autowired
	private StoreMapper storeMapper;
	
	@Autowired
	private StoreAddressMapper storeAddressMapper;
	
	public String encryptPassword(String password) {
		return passwordEncoder.encode(password);
	}

	// 회원 등록
	@Transactional
	@Override
	public int createdUser(UserVO userVO) {
		log.info("createdUserList()");
		
		// 비밀번호 암호화
		String password = userVO.getUserPw();
		log.info("암호화 전 : " + password);
		String encodePassword = passwordEncoder.encode(password);
		log.info("암호화 후 : " + encodePassword);
		
		UserVO vo = userVO;
		vo.setUserPw(encodePassword);
		vo.setUserActive(1);
		log.info(vo);
		
		int userCreated = userMapper.insertUser(vo);
		int userRoleCreated = userMapper.insertUserRole(vo.getUserId());
		log.info("회원 정보 " + userCreated + "행 등록 성공");
		log.info("회원 권한 " + userRoleCreated + "행 등록 성공");
				
		return 1;
	}

	// 회원 검색(회원 아이디)
	@Override
	public UserVO searchUser(String userId) {
		log.info("searchUserList()");
		UserVO vo = userMapper.selectUserByUserId(userId);
		return vo;
	}

	// 회원 정보 수정(비밀번호 제외)
	@Override
	public int modifyUser(UserVO userVO) {
		log.info("modifyUserList()");
		
		return userMapper.updateUser(userVO);
	}

	// 로그인
	@Override
	public int login(String userId, String userPw) {
		log.info("login()");
		UserVO vo = new UserVO();
		vo.setUserId(userId);
		vo.setUserActive(1);
		int result = userMapper.checkUserByUserId(vo);
		if(result == 1) {
			vo = userMapper.selectUserByUserId(userId);
			String encodePw = vo.getUserPw();
			
			if(!passwordEncoder.matches(userPw, encodePw)) {
				result = 0;
			}
		}
		
		return result;
	}

	// 회원 비밀번호 수정
	@Override
	public int modifyUserPw(UserVO userVO) {
		log.info("modifyUserPw()");
		log.info(userVO);
		UserVO vo = new UserVO();
		
		// 암호화
		String encodePw = passwordEncoder.encode(userVO.getUserPw());
		
		vo.setUserPw(encodePw);
		int result = 0;
		if(userVO.getUserEmail().isBlank() && userVO.getUserId() != null) {
			log.info("userEmail = null");
			vo.setUserId(userVO.getUserId());
			result = userMapper.updateUserPwByUserId(vo);
		} else if(userVO.getUserId() == null && !userVO.getUserEmail().isBlank()) {
			log.info("userId = null");
			vo.setUserEmail(userVO.getUserEmail());
			result = userMapper.updateUserPwByUserEmail(vo);
		}
		return result;
	}

	// 사용자 확인
	@Override
	public int checkUserByUserId(String userId, String type) {
		UserVO vo = new UserVO();
		vo.setUserId(userId);
		if(!type.equals("회원가입")) {
			vo.setUserActive(1);
		}
		log.info("active : " + vo.getUserActive());
		return userMapper.checkUserByUserId(vo);
	}

	// 아이디 찾기
	@Override
	public String searchUserId(String userEmail) {
		UserVO vo = userMapper.selectUserIdByUserEmail(userEmail);
		String userId = vo.getUserId();
		int index = userId.length() - 2;
		log.info(index);
		String chUserId = userId.substring(0, 2);
		for(int i = 0; i < index; i++) {
			chUserId += "*";
		}
		return chUserId;
	}

	// 회원 비활성화
	@Override
	public int deleteUser(int status, String userId) {
		log.info("deleteUser()");
		UserVO vo = new UserVO();
		vo.setUserActive(status);
		vo.setUserId(userId);
		return userMapper.updateActiveYnByUserId(vo);
	}

	// 회원 목록 삭제
	@Transactional(value = "transactionManager")
	@Override
	public int deleteUserList(int userActive) {
		log.info("deleteUserList()");
		log.info(userActive);
		
		List<UserVO> userList = userMapper.selectUserListByUserActive(userActive);
		
		int insertDelTableResult = 0;
		int deleteUserTableResult = 0;
		
		log.info(userList.size());
		
		for(int i = 0; i < userList.size(); i++) {
			String userId = userList.get(i).getUserId();
			log.info("userId : " + userId);
			UserVO vo = userList.get(i);
			log.info("vo : " + vo);
			insertDelTableResult += userDelMapper.insertUser(vo);
			deleteUserTableResult += userMapper.deleteUser(userId);
		}
		log.info("삭제 회원 " + insertDelTableResult + "행 등록");
		log.info("회원 정보 " + deleteUserTableResult + "행 삭제");
		
		return 1;
	}

	@Override
	public int permanentDeleteUserInfo() {
		// TODO Auto-generated method stub
		return 0;
	}

	// 사업자 등급 신청
	@Transactional
	@Override
	public int businessRequest(StoreVO storeVO, StoreAddressVO storeAddressVO) {
		log.info("businessUpgradeForm()");
		
		int insertStore = storeMapper.insertStore(storeVO);
		int storeId = storeVO.getStoreId();
		log.info("storeId : " + storeId);
		
		storeAddressVO.setStoreId(storeId);
		int insertStoreAddress = storeAddressMapper.insertStoreAddress(storeAddressVO);
		
		BusinessRequestVO businessRequestVO = new BusinessRequestVO();
		businessRequestVO.setStoreId(storeId);
		businessRequestVO.setUserId(storeVO.getUserId());
		businessRequestVO.setRequestApprovals(0);
		int insertBusinessRequest = businessRequestMapper.insertBusinessRequest(businessRequestVO);
		
		StoreApprovalsVO storeApprovalsVO = new StoreApprovalsVO();
		storeApprovalsVO.setStoreId(storeId);
		storeApprovalsVO.setStoreApprovals(0);
		int insertStoreApprovals = storeApprovalsMapper.insertStoreApprovals(storeApprovalsVO);
		
		log.info("Store : " + insertStore + "행 등록 성공");
		log.info("StoreAddress : " + insertStoreAddress + "행 등록 성공");
		log.info("businessRequest : " + insertBusinessRequest + "행 등록 성공" );
		log.info("StoreApprovals : " + insertStoreApprovals + "행 등록 성공");
		
		return 1;
	}
	
	// 사업자 등록 신청 정보 조회
	@Override
	public BusinessRequestInfoVO searchBusinessRequestInfo(int businessRequestId) {
		log.info("searchBusinessRequestInfo()");
		BusinessRequestInfoVO vo = businessRequestMapper.selectBusinessRequestByBusinessRequestId(businessRequestId);
		log.info(vo);
		return vo;
	}

	// 사업자 등록 신청 목록
	@Override
	public List<BusinessRequestInfoVO> searchBusinessRequestList(Pagination pagination) {
		log.info("searchBusinessRequestList()");
		return businessRequestMapper.selectBusinessRequestListByPagination(pagination);
	}

	// 사업자 등록 신청 목록 총 건수
	@Override
	public int getBusinessRequestTotalCount() {
		return businessRequestMapper.selectTotalCount();
	}
	
	
}
