package com.eatplatform.web.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	// ROLE_MEMBER 확인
	@GetMapping("/auth/member")
	public Map<String, Object> chechAuthorities(Principal principal) {
		log.info("checkAuthorities()");
		
		Map<String, Object> response = new HashMap<>();
		
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
		 // 인증되지 않은 경우
        if(principal == null) {
            response.put("isAuthenticated", false);
            response.put("role", null);
        } else {
            response.put("isAuthenticated", true);
            
            for(GrantedAuthority authority : authentication.getAuthorities()) {
            	if(authority.getAuthority().equals("ROLE_MEMBER")) {
            		response.put("role", "ROLE_MEMBER");
            	} else {
            		response.put("role", "OTHER");
            	}
            }
        }
        log.info("response : " + response);
		return response;
	}
	
}
