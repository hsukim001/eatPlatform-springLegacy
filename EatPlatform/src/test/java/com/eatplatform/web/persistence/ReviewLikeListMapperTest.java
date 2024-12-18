package com.eatplatform.web.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eatplatform.web.config.RootConfig;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
@Log4j
public class ReviewLikeListMapperTest {
	
	@Autowired
	private ReviewLikeListMapper reviewLikeListMapper;
	
	@Test
	public void test() {
		insertlike();
	}

	private void insertlike() {
		int result = reviewLikeListMapper.insert(7, "test1");
		log.info(result);
	}
	

}
