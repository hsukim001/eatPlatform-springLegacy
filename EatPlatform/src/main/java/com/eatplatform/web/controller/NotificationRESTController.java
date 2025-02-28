package com.eatplatform.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.eatplatform.web.service.NotificationService;

@RestController
@RequestMapping(value = "/notifications")
public class NotificationRESTController {

	@Autowired
	private NotificationService notificationService;
	
	@GetMapping(value = "/subscribe/{username}", produces = "text/event-stream")
	public SseEmitter subscribe(@PathVariable String username) {
		return notificationService.subscribe(username);
	}

}
