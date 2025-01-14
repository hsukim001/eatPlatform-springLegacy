package com.eatplatform.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.eatplatform.web.config.SecurityConfig;
import com.eatplatform.web.config.ServletConfig;
import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.service.ReservService;
import com.eatplatform.web.service.UserService;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServletConfig.class, SecurityConfig.class})
@WebAppConfiguration
@Log4j
public class ControllerTest {
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReservService reservService;
	
	private MockMvc mock;
	
	@Test
	public void test() {
		insert();
//		check();
	}
	
	private void insert() {
		log.info("insertTest");
		UserVO vo = new UserVO();
		vo.setUserId("store02");
		vo.setUserPw("store");
		vo.setUserEmail("test@test.com");
		vo.setUserName("store02");
		vo.setUserPhone("010-123-0000");
		
		log.info(vo);
		int result = userService.createdUser(vo);
		log.info("result : " + result);
	}
	
	private void check() {
		Date toDay = new Date();
		log.info(toDay);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String formatToday = dateFormat.format(toDay);
		log.info(formatToday);
	}
	
	private void reservInsert() {
		
	}
}
