package com.eatplatform.web.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eatplatform.web.config.RootConfig;
import com.eatplatform.web.domain.UserListVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링 JUnit test 연결
@ContextConfiguration(classes = { RootConfig.class }) // 설정 파일 연결
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
		
		log.info(result + "행 삭제");
	}


	private void update() {
		UserListVO userListVO = new UserListVO();

		userListVO.setUserId("test");
		userListVO.setUserPw("test123");
		userListVO.setUserEmail("mokmok@test.com");
		userListVO.setUserPhone("010-7716-7711");
		userListVO.setUserName("테스트 유저");

		log.info(userListVO);
		int result = userListMapper.updateUserList(userListVO);

		log.info(result + "행 수정");
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

		log.info(result + "행 등록");
	}

}
