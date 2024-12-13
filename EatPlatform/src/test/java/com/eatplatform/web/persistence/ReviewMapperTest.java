package com.eatplatform.web.persistence;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eatplatform.web.config.RootConfig;
import com.eatplatform.web.domain.ReviewVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
@Log4j
public class ReviewMapperTest {
	
	@Autowired
	private ReviewMapper reviewMapper;
	
	@Test
	public void test() {
		getAllreview();
	}

	private void getAllreview() {
		List<ReviewVO> list = reviewMapper.selectListByStoreId(1);
		log.info(list);
	}

}
