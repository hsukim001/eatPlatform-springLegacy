package com.eatplatform.web.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.eatplatform.web.config.SecurityConfig;
import com.eatplatform.web.config.ServletConfig;
import com.eatplatform.web.domain.ReservVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServletConfig.class, SecurityConfig.class})
@WebAppConfiguration
@Log4j
public class ServiceConcurrencyTest {
	
	@Autowired
	private ReservService reservService;
	
	@Test
	public void test() throws InterruptedException {
		int threadCount = 5;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);
		
		for(int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					ReservVO vo = new ReservVO();
//					vo.setUserId("test");
					vo.setStoreId(131);
					vo.setReservDate("2025-01-17");
					vo.setReservHour("15");
					vo.setReservMin("00");
					vo.setReservPersonnel(3);
					int reservLimit = 10;
					int result = reservService.createdReserv(vo, reservLimit);
					
					log.info(result + "입니다.");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			});
		}
		
		latch.await();
		executorService.shutdown();
	}
}
