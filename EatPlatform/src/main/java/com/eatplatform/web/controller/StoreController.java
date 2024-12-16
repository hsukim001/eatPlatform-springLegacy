package com.eatplatform.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.service.StoreService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping(value = "/store")
@Log4j
public class StoreController {

	@Autowired
	private StoreService storeService;

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

		// result 값을 모델에 담아서 JSP로 전달
		model.addAttribute("result", result);

		return "/store/register";
	}

	@GetMapping("/list")
	public String getStoresWithPaging(@RequestParam(defaultValue = "1") int pageNum, Model model) {
	    if (pageNum <= 0) {
	        pageNum = 1;
	    }
	    log.info("Current Page Number: " + pageNum);
	    int pageSize = 6;
	    List<StoreVO> stores = storeService.getStoresWithPaging(pageNum, pageSize);
	    List<StoreVO> recentStores = storeService.getStoresWithPaging(pageNum, pageSize); 
	    int totalStoresCount = storeService.getTotalStoresCount();
	    int totalPages = (int) Math.ceil((double) totalStoresCount / pageSize);

	    model.addAttribute("stores", stores);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("currentPage", pageNum);
	    model.addAttribute("recentStores", recentStores);
	    log.info(recentStores);

	    return "/store/list";
	}
}
