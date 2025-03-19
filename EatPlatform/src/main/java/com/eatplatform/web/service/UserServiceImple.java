package com.eatplatform.web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.domain.RoleListVO;
import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.persistence.UserAdminMapper;
import com.eatplatform.web.persistence.UserMapper;
import com.eatplatform.web.persistence.RoleListMapper;
import com.eatplatform.web.persistence.UserStoreMapper;

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
	private RoleListMapper roleListMapper;

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

		RoleListVO roleListVO = new RoleListVO();
		roleListVO.setUsername(userMemberVO.getUsername());
		roleListVO.setRoleName("ROLE_MEMBER");
		int resultRole = roleListMapper.insertUserRole(roleListVO);

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
			RoleListVO role = roleListMapper.selectUserRoleByUsername(vo.getUsername());
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
			int deleteWithdrowalUserRoleResult = roleListMapper.deleteWithdrowalUserRole();
			log.info("권한 정보 " + deleteWithdrowalUserRoleResult + "행 삭제");
		} else if(withdrwalStoreResult >= 1) {
			int deleteWithdrowalStoreUserRoleResult = roleListMapper.deleteWithdrowalStoreUserRole();
			log.info("권한 정보 " + deleteWithdrowalStoreUserRoleResult + "행 삭제");
		} else if(withdrwalAdminResult >= 1) {
			int deleteWithdrowalAdminUserRoleResult = roleListMapper.deleteWithdrowalAdminUserRole();
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

	@Override
	public String getStoreUserNameByUserId(String userName) {
		return userMapper.selectStoreUserNameByUserId(userName);
	}

}
