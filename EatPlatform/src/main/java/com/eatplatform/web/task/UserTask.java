package com.eatplatform.web.task;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eatplatform.web.persistence.UserDelMapper;
import com.eatplatform.web.service.UserService;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class UserTask {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserDelMapper userDelMapper;
	
	// 비활성화 회원 삭제 테이블 정보 저장 및 삭제
	@Scheduled(cron = "0 00 12 * * *") // 매일 12시 동작
	public void deleteUserInfo() {
		log.warn("============================");
		log.warn("Delete User Info Task Run");
		
		int userActiveYn = 0;
		userService.deleteUserList(userActiveYn);
	}
	
	// 보관 기간이 지난 회원 정보 영구 삭제
	@Scheduled(cron = "0 0 12 * * *")
	public void permanentDeleteUserInfo() {
		log.warn("============================");
		log.warn("Permanent Delete User Info Task Run");
		
		LocalDate toDay = LocalDate.now();
		LocalDate threeMonthsAftter = toDay.minusMonths(3);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String deadline = threeMonthsAftter.format(formatter);
		log.info(deadline);
		
		int result = userDelMapper.permanentDeleteUserInfo(deadline);
		log.info("보관기간이 지난 " + result + "개의 탈퇴 회원정보 영구 삭제");
	}
}
