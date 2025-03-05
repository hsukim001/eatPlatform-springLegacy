package com.eatplatform.web.service;

import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.eatplatform.web.domain.NotificationVO;

public interface NotificationService {
	
	SseEmitter subscribe(String username);
	List<NotificationVO> getUnreadNotifications(String username);
	void sendReviewNotification(String type, String username, String message);
	int updateNotification(int notificationId);
}
