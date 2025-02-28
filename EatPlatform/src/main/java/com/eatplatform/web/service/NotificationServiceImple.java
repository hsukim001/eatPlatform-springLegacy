package com.eatplatform.web.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.eatplatform.web.persistence.NotificationMapper;

import lombok.extern.log4j.Log4j;

@Service
@Transactional(value = "transactionManager")
@Log4j
public class NotificationServiceImple implements NotificationService {
	
	private static final long DEFAULT_TIMEOUT_MS = 60L * 1000 * 60;
	
	private static final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
	
	@Autowired
	private NotificationMapper notificationMapper;
	
    /**
     * 클라이언트가 알림을 구독하기 위한 메서드
     * @param username 사용자 이름
     * @return SseEmitter 클라이언트와의 연결을 관리하는 객체
     */
	@Override
	public SseEmitter subscribe(String username) {
		
		SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT_MS);
		
		try {
			sseEmitter.send(sseEmitter.event()
					.id(username)
					.name("sse")
					.data("success")
					);
		} catch (Exception e) {
			log.error("error : " + e.getMessage());
		}
		
		sseEmitters.put(username, sseEmitter);
		
		sseEmitter.onCompletion(() -> removeSseEmitter(username));
        sseEmitter.onTimeout(() -> removeSseEmitter(username));
        sseEmitter.onError((e) -> removeSseEmitter(username));
		
		return sseEmitter;
	}

	/**
     * SseEmitter를 삭제하는 메서드
     * @param username 사용자 이름
     */
	private void removeSseEmitter(String username) {
		if(sseEmitters.containsKey(username)) {
    		sseEmitters.remove(username);
    		log.info("Removed SseEmitter for username: " + username);
    	}	
		
	}

}
