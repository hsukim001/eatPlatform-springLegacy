package com.eatplatform.web.persistence;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eatplatform.web.config.RootConfig;
import com.eatplatform.web.config.SecurityConfig;
import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.domain.UserVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class, SecurityConfig.class })
@Log4j
public class UserMapperTest {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private ReservMapper reservMapper;

	@Test
	public void test() {
//		selectList();
		insert();
	}
	
	private void selectList() {
		log.info("selectList()");
		char userActive = 0;
		List<UserVO> list = new ArrayList<>();
		list = userMapper.selectUserListByUserActive(userActive);
		for(int i = 0; i < list.size(); i++) {
			log.info(list.get(i));
		}
	}
	
	private void insert() {
		ReservVO vo = new ReservVO();
		vo.setStoreId(166);
		vo.setUserId("hsukim");
		vo.setReservDate("2025-01-15");
		vo.setReservHour("00");
		vo.setReservMin("30");
		vo.setReservPersonnel(6);
		int reservLimit = 30;
		
//		int result = reservMapper.insert(vo, reservLimit);
//		log.info(result);
	}

}
