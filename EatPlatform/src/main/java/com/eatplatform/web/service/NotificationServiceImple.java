package com.eatplatform.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.eatplatform.web.domain.CustomUser;
import com.eatplatform.web.domain.NotificationVO;
import com.eatplatform.web.domain.ReplyVO;
import com.eatplatform.web.domain.ReservCancelVO;
import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.domain.ReviewVO;
import com.eatplatform.web.domain.StoreApprovalsVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.persistence.NotificationMapper;
import com.eatplatform.web.persistence.ReservMapper;
import com.eatplatform.web.persistence.ReviewMapper;
import com.eatplatform.web.persistence.StoreApprovalsMapper;
import com.eatplatform.web.persistence.StoreMapper;
import com.eatplatform.web.persistence.UserMapper;
import com.eatplatform.web.util.NotificationTemplate;

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
	private ReviewMapper reviewMapper;
	
	@Autowired
	private ReservMapper reservMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private StoreApprovalsMapper storeApprovalsMapper;
	
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
			e.printStackTrace();
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
     * 리뷰 등록 알림
     */
    @Override
    public void addReviewNotification(ReviewVO reviewVO) {
    	
    	StoreVO storeVO = storeMapper.selectStoreById(reviewVO.getStoreId());
    	
    	String message = String.format(NotificationTemplate.Messages.ADD_REVIEW, storeVO.getStoreName());
    	String url = NotificationTemplate.Url.STORE_DETAIL_URL + storeVO.getStoreId();
    	
    	sendNotification(NotificationTemplate.Types.ADD_REVIEW, storeVO.getStoreUserId(), message, url);
    }
    
	/**
	 * 댓글 등록 알림
	 */
	@Override
	public void addReplyNotification(ReplyVO replyVO) {
		
		ReviewVO reviewVO = reviewMapper.findReviewWithUsername(replyVO.getReviewId());
		StoreVO storeVO = storeMapper.selectStoreById(reviewVO.getStoreId());
		
		String message = String.format(NotificationTemplate.Messages.ADD_REPLY, storeVO.getStoreName());
		String url = NotificationTemplate.Url.STORE_DETAIL_URL + storeVO.getStoreId();
		
		sendNotification(NotificationTemplate.Types.ADD_REPLY, reviewVO.getUsername(), message, url);
	}

	/**
	 * 예약 등록 알림
	 */
	@Override
	public void addReservNotification(ReservVO reservVO) {
		
		StoreVO storeVO = storeMapper.selectStoreById(reservVO.getStoreId());
		UserVO userVO = userMapper.selectUserByUserId(reservVO.getUserId());
	
		String reservTime = reservVO.getReservHour() + ":" + reservVO.getReservMin();
		String message = String.format(NotificationTemplate.Messages.ADD_RESERV,
				storeVO.getStoreName(), reservVO.getReservDate(), reservTime);
		String url = NotificationTemplate.Url.RESERV_LIST_URL;
		
		sendNotification(NotificationTemplate.Types.ADD_RESERV, userVO.getUsername(), message, url);
		sendNotification(NotificationTemplate.Types.ADD_RESERV, storeVO.getStoreUserId(), message, url);
	}
    
    /**
     * 예약 취소 알림
     */
    @Override
	public void cancelReservNotification(List<ReservCancelVO> cancelList,
			@AuthenticationPrincipal CustomUser customUser) {
    	
    	log.info("cancelList : " + cancelList);
    	String url = NotificationTemplate.Url.RESERV_LIST_URL;
    	
    	// 사업자에게 알림을 한 번만 보내도록 flag 설정
    	boolean sentStoreNotification = false;
    	
    	boolean isStoreUser = customUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STORE"));
    	
    	// 권한 확인
    	String type = isStoreUser
    			? NotificationTemplate.Types.STORE_CANCEL_RESERV
    			: NotificationTemplate.Types.USER_CANCEL_RESERV;
    	
	    for(ReservCancelVO reservCancelVO : cancelList) {
	    	if(isStoreUser) {
	    		cancelNotification(reservCancelVO, type, customUser, url);
	    		
	    		if(!sentStoreNotification) {
	    			storeCancelNotification(reservCancelVO, type, customUser, url);
	    			// 사업자 알림 전송 완료 표시
	    			sentStoreNotification = true;
	    		}
	    	} else {
	    		cancelNotification(reservCancelVO, type, customUser, url);
	    	}
    	}
	}

    /**
     * 고객 예약 취소 알림
     * @param reservCancelVO
     * @param type
     * @param customUser
     * @param url
     */
    private void cancelNotification(ReservCancelVO reservCancelVO, String type, CustomUser customUser, String url) {
    	int reservId = reservCancelVO.getReservId();
		ReservVO reservVO = reservMapper.selectReservByReservId(reservId);
		StoreVO storeVO = storeMapper.selectStoreById(reservVO.getStoreId());
		UserVO userVO = userMapper.selectUserByUserId(reservVO.getUserId());
		
		String reservTime = reservVO.getReservHour() + ":" + reservVO.getReservMin();
		String message = String.format(NotificationTemplate.Messages.CANCEL_RESERV, 
				storeVO.getStoreName(), reservVO.getReservDate(), reservTime);
		
		// 사업자 예약 취소
	    if (customUser.getUsername().equals(storeVO.getStoreUserId())) {
	        sendNotification(type, userVO.getUsername(), message, url);
	    // 고객 예약 취소
	    } else {
	        sendNotification(type, storeVO.getStoreUserId(), message, url);
	    }
	}

	/**
	 * 사업자 예약 취소 알림
	 * @param reservCancelVO
	 * @param type
	 * @param customUser
	 * @param url
	 */
	private void storeCancelNotification(ReservCancelVO reservCancelVO, String type, CustomUser customUser, String url) {
		int reservId = reservCancelVO.getReservId();
		ReservVO reservVO = reservMapper.selectReservByReservId(reservId);
		StoreVO storeVO = storeMapper.selectStoreById(reservVO.getStoreId());
		
		String message = String.format(NotificationTemplate.Messages.STORE_CANCEL_RESERV,
				storeVO.getStoreName(), reservVO.getReservDate());
		
		sendNotification(type, storeVO.getStoreUserId(), message, url);
	}
	
	/**
	 * 가게 등록 승인 여부 알림
	 * 
	 */
	@Override
	public void storeApprovalNotification(int storeId) {
		
		StoreApprovalsVO storeApprovalsVO = storeApprovalsMapper.selectApprovalsByStoreId(storeId);
		StoreVO storeVO = storeMapper.selectStoreById(storeId);
		String url = NotificationTemplate.Url.BUSINESS_REQUEST_URL;
		
		if(storeApprovalsVO.getApprovals() == 1) {
			String type = NotificationTemplate.Types.STORE_APPROVED;
			String message = String.format(NotificationTemplate.Messages.STORE_APPROVED, storeVO.getStoreName());
			
			sendNotification(type, storeVO.getStoreUserId(), message, url);
			
		} else {
			String type = NotificationTemplate.Types.STORE_REJECTED;
			String message = String.format(NotificationTemplate.Messages.STORE_REJECTED, storeVO.getStoreName());
			
			sendNotification(type, storeVO.getStoreUserId(), message, url);
		}
		
	}
	
	/**
	 * 알림 저장 및 실시간 전송 메서드
	 * @param type
	 * @param receiver
	 * @param message
	 * @param url
	 */
	private void sendNotification(String type, String receiver, String message, String url) {
        createNotification(type, receiver, message, url);
        sendSseNotification(type, receiver, message);
    }
	
    /**
     * 알림을 DB에 저장하는 메서드
     * @param type 알림 유형
     * @param receiver 
     * @param message 
     * @param url
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
     * @param type 
     * @param receiver 
     * @param message 
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
            log.info("오프라인 : " + receiver);
        }
    }

}
