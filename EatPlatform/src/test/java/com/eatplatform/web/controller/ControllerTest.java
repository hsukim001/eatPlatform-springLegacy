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
import com.eatplatform.web.domain.StoreAddressVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.service.ReservService;
import com.eatplatform.web.service.StoreService;
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
	
	@Autowired
	private StoreService storeService;
	
	private MockMvc mock;
	
	@Test
	public void test() {
//		insertStore();
		insertUser();
//		check();
	}
	
	private void insertStore() {
		log.info("insertStore()");
		
		for(int i = 1; i < 131; i++) {
			StoreVO storeVO = new StoreVO();
			StoreAddressVO storeAddressVO = new StoreAddressVO();
			
			
			storeVO.setStoreName("강아지" + i);
			storeVO.setStorePhone("1111");
			storeVO.setBusinessHour("10:00 - 23:00");
			storeVO.setFoodCategory("한식");
			storeVO.setOwnerName("사장님" + i);
			storeVO.setReservLimit(10);
			storeVO.setSeat(10);
			storeVO.setUserId("store");
			storeVO.setStoreComment("test");
			storeVO.setDescription("test");
			
			storeAddressVO.setSigungu("강남구");
			storeAddressVO.setSido("서울특별시");
			storeAddressVO.setRoadAddress("서울특별시 강남구 가로수길" + i);
			storeAddressVO.setPostCode("12345");
			storeAddressVO.setJibunAddress("서울특별시 강남구 신사동 537-5");
			storeAddressVO.setBname1("");
			storeAddressVO.setBname2("신사동");
			storeAddressVO.setExtraAddress("신사동 강남아파트");
			storeAddressVO.setDetailAddress("");
			
			log.info(storeAddressVO);
			log.info(storeVO);
			
			int result = storeService.registerStore(storeVO, storeAddressVO);
		}
		
	}
	
	private void insertUser() {
		log.info("insertTest");
		UserVO vo = new UserVO();
		vo.setUserId("user");
		vo.setUserPw("user");
		vo.setUserEmail("user@test.com");
		vo.setUserName("사용자");
		vo.setUserPhone("010-0001-1111");
		
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
