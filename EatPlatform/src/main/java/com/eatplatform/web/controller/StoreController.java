package com.eatplatform.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatplatform.web.domain.MenuVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.service.MenuService;
import com.eatplatform.web.service.StoreService;
import com.eatplatform.web.util.BusinessHourUtil;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping(value = "/store")
@Log4j
public class StoreController {

	@Autowired
	private StoreService storeService;
	
	@Autowired
	private MenuService menuService;

	@GetMapping("/newStore")
	public String newStore(HttpServletRequest request, Model model) {
		log.info("newStore()");
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");

		StoreVO storeVO = new StoreVO();
		storeVO.setUserId(userId);
		log.info(userId);

		model.addAttribute("storeVO", storeVO);

		return "/store/newStore";
	}

	@PostMapping("/register")
	public String register(StoreVO storeVO, Model model) {
		int result = storeService.registerStore(storeVO);

		model.addAttribute("result", result);

		return "/store/register";
	}

	@GetMapping("/list")
	public String getStoresWithPaging(@RequestParam(defaultValue = "1") int pageNum,
															@RequestParam(value = "keyword", required = false) String keyword,
															Model model) {
		if (pageNum <= 0) {
			pageNum = 1;
		}
		log.info("Current Page Number: " + pageNum);
		int pageSize = 6;
		
		List<String> keywords = new ArrayList<>();
	    if (keyword != null && !keyword.isEmpty()) {
	        keywords = Arrays.asList(keyword.split(" "));
	    }
		log.info(keywords);
		log.info("keywords type: " + keywords.getClass().getName());
		List<StoreVO> recentStores = storeService.getStoresWithPaging(pageNum, pageSize, keywords);
		int totalStoresCount = storeService.getTotalStoresCount(keywords);
		int totalPages = (int) Math.ceil((double) totalStoresCount / pageSize);

		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("recentStores", recentStores);
		model.addAttribute("keyword", keyword);
		log.info(recentStores);

		return "/store/list";
	}

	@GetMapping("/updateStore")
	public String updateStore(@RequestParam("storeId") int storeId, Model model, HttpServletRequest request) {
		StoreVO storeVO = storeService.selectStoreById(storeId);

		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute("userId");
		String dbUserId = storeService.getUserIdByStoreId(storeVO.getStoreId());

	    
		log.info("sessionUserId : " + sessionUserId + "// dbUserId : " + dbUserId);
		
		if (dbUserId != null && dbUserId.equals(sessionUserId)) {
		List<String> categories = Arrays.asList(
				"한식", "중식", "일식", "양식", "아시안", "치킨", "피자", "패스트푸드", "카페/디저트"
				);
		
		String businessHour = storeVO.getBusinessHour();
	    if (businessHour == null || !businessHour.contains(" - ")) {
			String errHandler = "invalidTimeFormat";
			log.info("잘못된 시간 포맷");
			model.addAttribute("errHandler", errHandler);
	        return "/store/errHandler";
	    }

		String[] times = BusinessHourUtil.splitBusinessHour(businessHour);
		String startTime = times[0];  
	    String endTime = times[1];
		
		model.addAttribute("storeVO", storeVO);
		model.addAttribute("categories", categories);
	    model.addAttribute("startTime",startTime);
	    model.addAttribute("endTime",endTime);
		return "/store/updateStore";
		} else {
			String errHandler = "otherUser";
			log.info(errHandler);
			log.info("잘못된 User 접근");
			model.addAttribute("errHandler", errHandler);
		return "/store/errHandler";
		}
	}

	@PostMapping("/modify")
	public String modify(@ModelAttribute StoreVO storeVO, Model model) {
		log.info("modify()");
		log.info(storeVO);
			int result = storeService.modifyStore(storeVO);
			if (result == 1) {
				log.info("가게 수정 성공");
			}
			log.info(result);
			model.addAttribute("result", result);
		return "/store/modify";
	}
	
	@GetMapping("/detail")
	public String detail(@RequestParam("storeId") int storeId, Model model) {
		log.info("detail");
		StoreVO storeVO = storeService.selectStoreById(storeId);
		List<MenuVO> menuVO = menuService.selectMenuByStoreId(storeId);
		log.info(menuVO);
		String businessHour = storeVO.getBusinessHour();
		String[] times = BusinessHourUtil.splitBusinessHour(businessHour);

	    if (businessHour == null || !businessHour.contains(" - ")) {
			String errHandler = "invalidTimeFormat";
			log.info("잘못된 시간 포맷");
			model.addAttribute("errHandler", errHandler);
	        return "/store/errHandler";   	
	    }
		String startTime = times[0];  
	    String endTime = times[1];
	    
	    model.addAttribute("startTime",startTime);
	    model.addAttribute("endTime",endTime);
		model.addAttribute("storeVO", storeVO);
		model.addAttribute("menuVO", menuVO);
		return "/store/detail";
	}
}
