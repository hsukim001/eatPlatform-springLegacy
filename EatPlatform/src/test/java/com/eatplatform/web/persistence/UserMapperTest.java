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
import com.eatplatform.web.domain.UserVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class, SecurityConfig.class })
@Log4j
public class UserMapperTest {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void test() {
		selectList();
	}
	
	private void selectList() {
		log.info("selectList()");
		char userActiveYn = 'N';
		List<UserVO> list = new ArrayList<>();
		list = userMapper.selectUserListByUserActiveYn(userActiveYn);
		for(int i = 0; i < list.size(); i++) {
			log.info(list.get(i));
		}
	}

}
