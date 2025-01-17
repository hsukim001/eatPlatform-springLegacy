package com.eatplatform.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.service.ReservService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/reserv")
@Log4j
public class ReservController {
	
	@Autowired
	private ReservService reservService;
	
	// 목록 페이지 호출
	@GetMapping("/list")
	public void reservList() {
		log.info("reservList()");
	}
	
	@PostMapping("/register")
	public String registerPOST(ReservVO reservVO, HttpServletRequest request) {
		log.info("registerPOST()");
		ReservVO vo = reservVO;
		
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		
		vo.setUserId(userId);
		
		int result = reservService.createdReserv(vo, 1);
		if(result == 1) {
			log.info("예약 등록 성공");
		}
		return "redirect:/reserv/list";
	}
}
