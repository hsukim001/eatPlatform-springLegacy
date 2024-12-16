package com.eatplatform.web.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eatplatform.web.config.RootConfig;
import com.eatplatform.web.domain.UserVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class) // ������ JUnit test ����
@ContextConfiguration(classes = { RootConfig.class }) // ���� ���� ����
@Log4j
public class UserMapperTest {

	@Autowired
	private UserMapper userListMapper;

	@Test
	public void test() {
//		insertUser();
//		select();
//		update();
		deleteUser();
	}


	private void deleteUser() {
		String userId = "test2";
		int result = userListMapper.deleteUser(userId);
		
		log.info(result + "�� ����");
	}


	private void update() {
		UserVO userListVO = new UserVO();

		userListVO.setUserId("test");
		userListVO.setUserPw("test123");
		userListVO.setUserEmail("mokmok@test.com");
		userListVO.setUserPhone("010-7716-7711");
		userListVO.setUserName("�׽�Ʈ ����");

		log.info(userListVO);
		int result = userListMapper.updateUser(userListVO);

		log.info(result + "�� ����");
	}

	private void select() {
		UserVO userListVO = new UserVO();
		String userId = "test2";
		userListVO = userListMapper.selectUserByUserId(userId);
		log.info(userListVO);
	}

	private void insertUser() {
		UserVO userListVO = new UserVO();

		userListVO.setUserId("test2");
		userListVO.setUserPw("test2");
		userListVO.setUserEmail("test2@test.com");
		userListVO.setUserPhone("010-1111-0001");
		userListVO.setUserName("test2 name");
		userListVO.setUserAuth("ROLE_MEMBER");
		userListVO.setUserActiveYn('Y');

		log.info(userListVO);
		int result = userListMapper.insertUser(userListVO);

		log.info(result + "�� ���");
	}

}
