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
import com.eatplatform.web.domain.ReservCancelVO;
import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.domain.ReviewVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.persistence.NotificationMapper;
import com.eatplatform.web.persistence.ReservMapper;
import com.eatplatform.web.persistence.StoreMapper;
import com.eatplatform.web.persistence.UserMapper;

import lombok.extern.log4j.Log4j;

@Service
@Transactional(value = "transactionManager")
@Log4j
public class NotificationServiceImple implements NotificationService {
	
	private static final long DEFAULT_TIMEOUT_MS = 60L * 60L * 1000;
	
	private static final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
	
	@Autowired
	private NotificationMapper notificationMapper;
	
	@Autowired
	private StoreMapper storeMapper;
	
	@Autowired
	private ReservMapper reservMapper;
	
	@Autowired
	private UserMapper userMapper;
	
    /**
     * 클라이언트가 알림을 구독하기 위한 메서드
     * @param receiver 사용자 이름
     * @return SseEmitter 클라이언트와의 연결을 관리하는 객체
     */
	@Override
	public SseEmitter subscribe(String receiver) {
		
		SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT_MS);
		try {
			sseEmitter.send(SseEmitter.event()
					.id(receiver)
					.name("sse")
					.data("success")
					);
		} catch (Exception e) {
			log.error("error : " + e.getMessage());
			
			if (sseEmitter != null) {
	            sseEmitter.completeWithError(e);
	        }
	        return null;
		}
		
		sseEmitters.put(receiver, sseEmitter);
		
		sseEmitter.onCompletion(() -> removeSseEmitter(receiver));
        sseEmitter.onTimeout(() -> removeSseEmitter(receiver));
        sseEmitter.onError((e) -> removeSseEmitter(receiver));
		
		return sseEmitter;
	}

	 /**
     * 알림을 DB에서 읽지 않은 알림만 조회
     * @param receiver 사용자 이름
     * @return 읽지 않은 알림 리스트
     */
    @Override
    public List<NotificationVO> getNotificationsByReceiver(String receiver) {
        return notificationMapper.selectNotificationsByReceiver(receiver);
    }

	/**
	 * 알림을 읽음 처리로 업데이트하는 메서드
	 */
	@Override
	public int updateNotification(NotificationVO notificationVO) {
		
		if ("Y".equals(notificationVO.getRead())) {
			log.info("이미 읽은 알림");
		}
		return notificationMapper.update(notificationVO);
	}

    /**
     * SseEmitter를 삭제하는 메서드
     * @param receiver 사용자 이름
     */
    private void removeSseEmitter(String receiver) {
    	if(sseEmitters.containsKey(receiver)) {
    		sseEmitters.remove(receiver);
    		log.info("SseEmitter 삭제: " + receiver);
    	}
    }
    
    /**
     * 리뷰 등록 알림을 전송하고 저장하는 메서드
     */
    @Override
    public void addReviewNotification(ReviewVO reviewVO) {
    	
    	String type = "addReview";
    	StoreVO storeVO = storeMapper.selectStoreById(reviewVO.getStoreId());
    	String receiver = storeVO.getStoreUserId();
    	String storeName = storeVO.getStoreName();
    	String message = String.format("'%s'에 리뷰가 등록되었습니다.", storeName);
    	String url = "/store/detail?storeId=" + storeVO.getStoreId();
    	
    	createNotification(type, receiver, message, url);

        sendSseNotification(type, receiver, message);
    }
    
    /**
     * 예약 취소 알림을 전송하고 저장하는 메서드
     */
    @Override
	public void cancelReservNotification(List<ReservCancelVO> cancelList) {
    	
    	for(ReservCancelVO reservCancelVO : cancelList) {
    		int reservId = reservCancelVO.getReservId();
    		
    		ReservVO reservVO = reservMapper.selectReservByReservId(reservId);
    		
    		StoreVO storeVO = storeMapper.selectStoreById(reservVO.getStoreId());
    		String storeName = storeVO.getStoreName();
    		
    		String type = "cancelReserv";
    		
    		UserVO userVO = userMapper.selectUserByUserId(reservVO.getUserId());
    		String customer = userVO.getUsername();
    		
    		String customerMessage = String.format("'%s'님의 '%s'예약이 취소되었습니다.", customer, storeName);
    		
    		String url = "/reserv/list";
    		
    		// 고객 예약 취소 알림
    		createNotification(type, customer, customerMessage, url);
    		sendSseNotification(type, customer, customerMessage);
    		
    		String storeUser = storeVO.getStoreUserId(); 
    		
    		String reservDate = reservVO.getReservDate();
    		String reservTime = reservVO.getReservHour() + ":" + reservVO.getReservMin();
    		String storeMessage = String.format("'%s'의 '%s'일 '%s' 예약이 취소되었습니다.", storeName, reservDate, reservTime);
    		
    		// 사업자 예약 취소 알림
    		createNotification(type, storeUser, storeMessage, url);
    		sendSseNotification(type, storeUser, storeMessage);
    		
    	}
    	
	}

    /**
     * 알림을 DB에 저장하는 메서드
     * @param type 알림 유형
     * @param receiver 사용자 이름
     * @param message 알림 메시지
     */
    private void createNotification(String type, String receiver, String message, String url) {
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setType(type);
        notificationVO.setReceiver(receiver);
        notificationVO.setMessage(message);
        notificationVO.setUrl(url);

        notificationMapper.insert(notificationVO);
    }

    /**
     * SSE를 통해 알림을 전송하는 메서드
     * @param type 알림 유형
     * @param receiver 사용자 이름사용자 이름
     * @param message 알림 메시지
     */
    private void sendSseNotification(String type, String receiver, String message) {
        SseEmitter sseEmitter = sseEmitters.get(receiver);
        Map<String, String> data = new HashMap<>();
		data.put("receiver", receiver);
		data.put("type", type);
		data.put("message", message);

        if (sseEmitter != null) {
            try {
                sseEmitter.send(SseEmitter.event()
                        .name("messageEvent")
                        .data(data)
                );
            } catch (Exception e) {
                log.error("알림 전송 중 오류 발생: " + e.getMessage());
                removeSseEmitter(receiver);
            }
        } else {
            log.info("SseEmitter에 해당 아이디가 존재하지 않음: " + receiver);
        }
    }

}
