package com.eatplatform.web.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/access")
public class AccessRESTController {
	
	// Spring Security 인증 여부 확인
	@GetMapping("/auth/status")
	public ResponseEntity<?> checkAuthStatus(Principal principal) {
		return ResponseEntity.ok(principal != null);
	}
	
}
