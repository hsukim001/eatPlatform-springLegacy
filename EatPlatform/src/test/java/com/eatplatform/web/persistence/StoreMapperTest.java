package com.eatplatform.web.persistence;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eatplatform.web.config.RootConfig;
import com.eatplatform.web.domain.StoreVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class) // Spring JUnit test
@ContextConfiguration(classes = { RootConfig.class }) 
@Log4j
public class StoreMapperTest {

	@Autowired
	private StoreMapper storeMapper;
	
	@Test
	public void test() {
		insertStore();
	}

	private void insertStore() {
		StoreVO storeVO = new StoreVO();
		storeVO.setUserId("insertTest");
		storeVO.setStorePhone("010-insert-test");
		storeVO.setOwnerName("insertTestOnwer");
		storeVO.setBusinessHour("08000000");
		storeVO.setStoreComment("짧은 소개 insert test");
		storeVO.setDescription("상세 설명 insert test");
		log.info("storeVO : " + storeVO);
		log.info("storeMapper: " + storeMapper);
		
		int result = storeMapper.insertStore(storeVO);
		log.info("result : " + result);
		log.info(result + "행 추가");
	}
}
