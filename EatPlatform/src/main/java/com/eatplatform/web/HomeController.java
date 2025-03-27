package com.eatplatform.web;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatplatform.web.domain.CustomUser;

import lombok.extern.log4j.Log4j;

/**
 * Handles requests for the application home page.
 */
@Controller
@Log4j
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, @RequestParam(value = "logout", required = false) String logout, 
			@RequestParam(value = "accessDenied", required = false) String accessDenied,
			@AuthenticationPrincipal CustomUser customUser) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		if(customUser != null) {
			boolean isMember = customUser.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_MEMBER"));
			if (isMember) {
		        // 회원(ROLE_MEMBER)으로 로그인한 경우
		        model.addAttribute("userId", customUser.getUser().getUserId());
		    } else {
		        // ROLE_STORE 또는 다른 권한으로 로그인한 경우
		        model.addAttribute("userId", 0);
		    }
		} else {
		    // 비로그인 상태
		    model.addAttribute("userId", 0);
		}
		
		// 로그아웃이 발생한 경우, 로그아웃 메시지를 모델에 추가하여 전달
		if(logout != null) {
			log.info("로그아웃 !!");
			model.addAttribute("message", "로그아웃 되었습니다!");
		}
		
		if(accessDenied != null) {
			log.info("접근 권한이 없습니다.");
			model.addAttribute("message", "잘못된 접근입니다.");
		}
		
		return "index";
	}

}
