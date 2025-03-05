package com.eatplatform.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.eatplatform.web.domain.CustomUser;
import com.eatplatform.web.domain.NotificationVO;
import com.eatplatform.web.service.NotificationService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping(value = "/notifications")
@Log4j
public class NotificationRESTController {

	@Autowired
	private NotificationService notificationService;
	
	@GetMapping(value = "/subscribe/{username}", produces = "text/event-stream; charset=utf-8")
	public SseEmitter subscribe(@PathVariable String username) {
		return notificationService.subscribe(username);
	}
	
	@GetMapping("/getUnreadNotifications")
	public ResponseEntity<List<NotificationVO>> getUnreadNotifications(@AuthenticationPrincipal CustomUser customUser) {
        String username = customUser.getUsername();  // 로그인한 사용자 ID
        
        List<NotificationVO> notifications = notificationService.getUnreadNotifications(username);
        return ResponseEntity.ok(notifications);
	}

	@PostMapping("/read/{notificationId}")
	public int updateNotifications(@PathVariable int notificationId) {
		return notificationService.updateNotification(notificationId);
	}

}
