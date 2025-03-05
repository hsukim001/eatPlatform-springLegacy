package com.eatplatform.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.eatplatform.web.domain.NotificationVO;
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
			sseEmitter.send(SseEmitter.event()
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
     * 알림을 DB에서 읽지 않은 알림만 조회
     * @param username 사용자 이름
     * @return 읽지 않은 알림 리스트
     */
    @Override
    public List<NotificationVO> getUnreadNotifications(String username) {
        return notificationMapper.findUnreadNotificationsByUsername(username);
    }

    
    /**
     * 리뷰 알림을 전송하고 저장하는 메서드
     */
    @Override
    public void sendReviewNotification(String type, String username, String message) {

    	createNotification(type, username, message);

        sendSseNotification(type, username, message);
    }

    /**
     * 알림을 DB에 저장하는 메서드
     * @param type 알림 유형
     * @param username 사용자 이름
     * @param message 알림 메시지
     */
    private void createNotification(String type, String username, String message) {
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setType(type);
        notificationVO.setUsername(username);
        notificationVO.setMessage(message);

        notificationMapper.insert(notificationVO);
    }

    /**
     * SSE를 통해 알림을 전송하는 메서드
     * @param type 알림 유형
     * @param username 사용자 이름사용자 이름
     * @param message 알림 메시지
     */
    private void sendSseNotification(String type, String username, String message) {
        SseEmitter sseEmitter = sseEmitters.get(username);
        Map<String, String> data = new HashMap<>();
		data.put("username", username);
		data.put("message", message);

        if (sseEmitter != null) {
            try {
                sseEmitter.send(SseEmitter.event()
                        .name(type)
                        .data(data)
                );
            } catch (Exception e) {
                log.error("알림 전송 중 오류 발생: " + e.getMessage());
                removeSseEmitter(username);
            }
        } else {
            log.info("SseEmitter에 해당 아이디가 존재하지 않음: " + username);
        }
    }

    /**
     * 알림을 읽음 처리로 업데이트하는 메서드
     * @param notificationId 알림 ID
     * @return 업데이트된 알림의 갯수
     */
    @Override
    public int updateNotification(int notificationId) {
        return notificationMapper.update(notificationId);
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
