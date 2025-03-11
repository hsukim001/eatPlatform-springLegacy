package com.eatplatform.web.service;

import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.eatplatform.web.domain.NotificationVO;
import com.eatplatform.web.domain.ReviewVO;

public interface NotificationService {
	
	SseEmitter subscribe(String username);
	List<NotificationVO> getUnreadNotifications(String username);
	void reviewNotification(ReviewVO reviewVO);
	int updateNotification(String url);
}
