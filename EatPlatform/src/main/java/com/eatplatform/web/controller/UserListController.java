package com.eatplatform.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eatplatform.web.domain.UserListVO;
import com.eatplatform.web.service.UserListService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/userList")
@Log4j
public class UserListController {
	
	@Autowired
	private UserListService userListService;
	
	// 회원 가입 유형 선택 페이지 이동
	@GetMapping("/flag")
	public void flag() {
		log.info("flag()");
	}
	
	// 회원 가입 페이지 이동
	@GetMapping("/register")
	public void registerGET(Model model, int flagNum) {
		log.info("registerGET()");
		
		model.addAttribute("flagNum", flagNum);
	}
	
	// 회원 등록
	@PostMapping("/register")
	public String registerPOST(UserListVO userListVO, int flagNum) {
		log.info("registerPOST()");
		log.info(flagNum);
		
		int result = userListService.createdUserList(userListVO, flagNum);
		
		if(result == 1) {
			log.info("회원 등록 성공");
		}
		
		return "redirect:/";
	}
	
	// 회원 상세(수정) 페이지 이동
	@GetMapping("/detail")
	public void detail(Model model) {
		log.info("detail()");
		String userId = "user";
		
		UserListVO vo = userListService.searchUserList(userId);
		model.addAttribute("vo", vo);
		log.info(vo);
	}
	
	// 회원 정보 수정
	@PostMapping("/modify")
	public String modify(UserListVO userListVO) {
		log.info("modify()");
		UserListVO vo = userListVO;
		vo.setUserId("user"); // session userId (추후 변경)
		int result = userListService.modifyUserList(vo);
		
		if(result == 1) {
			log.info("회원 정보 수정 성공");
		}
		
		return "redirect:/userList/detail";
	}
}
