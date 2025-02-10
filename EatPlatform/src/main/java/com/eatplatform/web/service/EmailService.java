package com.eatplatform.web.service;


import com.eatplatform.web.domain.EmailVO;
import com.eatplatform.web.domain.UserVO;

public interface EmailService {
	
	// 회원가입 인증 코드
	EmailVO sendEmailByAuthCode(EmailVO emailVO);
	// 인증코드 확인
	EmailVO checkAuthCode(EmailVO emailVO, String checkCode);
	// 비밀번호 찾기
	EmailVO sendSearchUser(UserVO userMemberVO, String mailType);
}
