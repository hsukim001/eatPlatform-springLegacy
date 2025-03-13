package com.eatplatform.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	/**
	 * 로그인 시 SSE 연결
	 * @param receiver 로그인 아이디
	 * @return SseEmitter를 통해 클라이언트와 실시간으로 알림을 전송할 수 있는 객체
	 */
	@GetMapping(value = "/subscribe/{receiver}", produces = "text/event-stream; charset=utf-8")
	public SseEmitter subscribe(@PathVariable String receiver) {
		return notificationService.subscribe(receiver);
	}
	
	/**
	 * 로그인 시 전체 알림 조회
	 * @param receiver 로그인 아이디
	 * @return 로그인한 아이디의 전체 알림
	 */
	@GetMapping("/{receiver}")
	public ResponseEntity<List<NotificationVO>> getNotifications(@PathVariable String receiver) {
        List<NotificationVO> notifications = notificationService.getNotificationsByReceiver(receiver);
        return ResponseEntity.ok(notifications);
	}

	/**
	 * 알림 읽음 상태 변경
	 * @param response notificationId의 데이터가 담긴 객체
	 * @param customUser 로그인 아이디
	 * @return notificationVO(notificationId, receiver, read, url)
	 */
	@PostMapping("/updateRead")
	public int updateNotifications(@RequestBody NotificationVO response,
			@AuthenticationPrincipal CustomUser customUser) {
		String receiver = customUser.getUsername();
		response.setReceiver(receiver);
		return notificationService.updateNotification(response);
	}

}
