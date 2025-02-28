package com.eatplatform.web.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
	
	SseEmitter subscribe(String username);

}
