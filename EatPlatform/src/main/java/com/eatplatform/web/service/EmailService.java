package com.eatplatform.web.service;


import com.eatplatform.web.domain.EmailVO;

public interface EmailService {
		
	EmailVO sendEmailByAuthCode(EmailVO emailVO);
	
	EmailVO checkAuthCode(EmailVO emailVO, String checkCode);
}
