package com.eatplatform.web.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/access")
@Log4j
public class AccessRESTController {
	
	// Spring Security 인증 여부 확인
	@GetMapping("/auth/status")
	public ResponseEntity<?> checkAuthStatus(Principal principal) {
		log.info("checkAuthStatus()");
		
		return ResponseEntity.ok(principal != null);
	}
	
	// username 확인
	@GetMapping("/auth/username")
	public ResponseEntity<?> getCurrentUser(Principal principal) {
		if (principal != null) {
			log.info("getCurrentUser()");
			
			Map<String, Object> userInfo = new HashMap<>();
			userInfo.put("username", principal.getName());
			userInfo.put("isAuthenticated", true);
			return ResponseEntity.ok(userInfo);
		} else {
			// 로그인되지 않은 상태
			Map<String, Object> userInfo = new HashMap<>();
			userInfo.put("isAuthenticated", false);
			return ResponseEntity.ok(userInfo);
		}
	}
	
}
