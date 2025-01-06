package com.eatplatform.web.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eatplatform.web.service.UserService;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class UserCheckTask {
	
	@Autowired
	UserService userService;
	
	// 비활성화 회원 삭제 테이블 정보 저장 및 삭제
	@Scheduled(cron = "0 0 12 * * *") // 매일 12시 동작
	public void deleteUser() {
		log.warn("============================");
		log.warn("Delete User Task Run");
		
		char userActiveYn = 'N';
		userService.deleteUserList(userActiveYn);
	}
}
