package com.eatplatform.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatplatform.web.domain.MenuVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.service.MenuService;
import com.eatplatform.web.service.StoreService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping(value = "/store/menu")
@Log4j
public class MenuController {

	@Autowired
	private MenuService menuService;
	
	@Autowired
	private StoreService storeService;
	

	@GetMapping("/newMenu")
	public String newMenu(@RequestParam("storeId") int storeId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
		log.info("newMenu()");
		
		String currentUserId = userDetails.getUsername();
		String dbUserId = storeService.getUserIdByStoreId(storeId);
		log.info("currentUserId : " + currentUserId + "// dbUserId : " + dbUserId);
		
		if (dbUserId != null && dbUserId.equals(currentUserId)) {
			// jsp로 storeId 전달을 위한 세팅
			MenuVO menuVO = new MenuVO();
			menuVO.setStoreId(storeId);
			log.info(storeId);

			// jsp로 storeName 전달을 위한 세팅
			StoreVO storeVO = storeService.selectStoreById(storeId);
			String storeName = storeVO.getStoreName();
			log.info(storeName);
			
			int representMenuCount = menuService.getRepresentMenuCountByStoreId(storeId);
						
			model.addAttribute("storeName", storeName);
			model.addAttribute("menuVO", menuVO);
			model.addAttribute("representMenuCount", representMenuCount);
			return "/store/menu/newMenu";
		} else {
			String errHandler = "otherUser";
			model.addAttribute("errHandler", errHandler);
			return "/store/errHandler";
		}

	}

	

	@PostMapping("/register")
	public String register(MenuVO menuVO, Model model, @AuthenticationPrincipal UserDetails userDetails) {
		String currentUserId = userDetails.getUsername();
		String dbUserId = storeService.getUserIdByStoreId(menuVO.getStoreId());
		
		if (dbUserId != null && dbUserId.equals(currentUserId)) {

			int result = menuService.registerMenu(menuVO);
			log.info(menuVO);

			model.addAttribute("result", result);

			return "/store/menu/register";
		} else {
			String errHandler = "otherUser";
			log.info(errHandler);
			log.info("잘못된 User 접근");
			model.addAttribute("errHandler", errHandler);
			return "/store/errHandler";
		}
	}
}
