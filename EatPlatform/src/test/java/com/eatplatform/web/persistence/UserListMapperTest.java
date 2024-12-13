package com.eatplatform.web.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eatplatform.web.config.RootConfig;
import com.eatplatform.web.domain.UserListVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class) // ������ JUnit test ����
@ContextConfiguration(classes = { RootConfig.class }) // ���� ���� ����
@Log4j
public class UserListMapperTest {

	@Autowired
	private UserListMapper userListMapper;

	@Test
	public void test() {
//		insertUser();
//		select();
//		update();
		deleteUser();
	}


	private void deleteUser() {
		String userId = "test2";
		int result = userListMapper.deleteUserList(userId);
		
		log.info(result + "�� ����");
	}


	private void update() {
		UserListVO userListVO = new UserListVO();

		userListVO.setUserId("test");
		userListVO.setUserPw("test123");
		userListVO.setUserEmail("mokmok@test.com");
		userListVO.setUserPhone("010-7716-7711");
		userListVO.setUserName("�׽�Ʈ ����");

		log.info(userListVO);
		int result = userListMapper.updateUserList(userListVO);

		log.info(result + "�� ����");
	}

	private void select() {
		UserListVO userListVO = new UserListVO();
		String userId = "test2";
		userListVO = userListMapper.selectUserListByUserId(userId);
		log.info(userListVO);
	}

	private void insertUser() {
		UserListVO userListVO = new UserListVO();

		userListVO.setUserId("test2");
		userListVO.setUserPw("test2");
		userListVO.setUserEmail("test2@test.com");
		userListVO.setUserPhone("010-1111-0001");
		userListVO.setUserName("test2 name");
		userListVO.setUserAuth("ROLE_MEMBER");
		userListVO.setUserActiveYn('Y');

		log.info(userListVO);
		int result = userListMapper.insertUserList(userListVO);

		log.info(result + "�� ���");
	}

}
