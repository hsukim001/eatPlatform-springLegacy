package com.eatplatform.web.service;

import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.eatplatform.web.domain.CustomUser;
import com.eatplatform.web.domain.NotificationVO;
import com.eatplatform.web.domain.ReplyVO;
import com.eatplatform.web.domain.ReservCancelVO;
import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.domain.ReviewVO;

public interface NotificationService {
	
	SseEmitter subscribe(String receiver);
	List<NotificationVO> getNotificationsByReceiver(String receiver);
	int updateNotification(NotificationVO notificationVO);
	
	void addReviewNotification(ReviewVO reviewVO);
	void addReplyNotification(ReplyVO replyVO);
	void addReservNotification(ReservVO reservVO);
	void cancelReservNotification(List<ReservCancelVO>cancelList, CustomUser customUser);
	void storeApprovalNotification(int storeId);
	void businessRequestNotification(int businessRequestId);
}
