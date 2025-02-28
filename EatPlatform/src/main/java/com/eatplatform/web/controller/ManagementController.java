package com.eatplatform.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatplatform.web.domain.CustomUser;
import com.eatplatform.web.domain.MenuVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.service.ManagementService;
import com.eatplatform.web.service.MenuService;
import com.eatplatform.web.service.StoreService;
import com.eatplatform.web.util.BusinessHourUtil;
import com.eatplatform.web.util.PageMaker;
import com.eatplatform.web.util.Pagination;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/management")
public class ManagementController {
	
	@Autowired
	private ManagementService managementService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private MenuService menuService;
	
	/**
	 * @param Model model
	 * @param Pagination pagination
	 * @param CustomUser customUser
	 */
	@GetMapping("/store/list")
	public void managementStoreList(Model model, Pagination pagination, @AuthenticationPrincipal CustomUser customUser) {
		log.info("managementStoreList()");
		String username = customUser.getUsername();
		
		List<StoreVO> storeList = managementService.searchStoreList(pagination, username);
		int totalCount = managementService.getTotalStoresCount(username);
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		pageMaker.setTotalCount(totalCount);
		
		model.addAttribute("list", storeList);
		model.addAttribute("pageMaker", pageMaker);
		
	}

	/**
	 * @param Model model
	 * @param int storeId
	 * @return String
	 */
	@GetMapping("/store/detail")
	public String managementStoreDetail(Model model, @RequestParam("storeId") int storeId) {
		log.info("managementStoreDetail()");
		StoreVO storeVO = storeService.selectStoreById(storeId);
		List<MenuVO> menuVO = menuService.selectMenuByStoreId(storeId);
		
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
		
		int representMenuCount = menuService.getRepresentMenuCountByStoreId(storeId);
		
		model.addAttribute("storeInfo", storeVO);
		model.addAttribute("menuInfo", menuVO);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("representMenuCount", representMenuCount);
		return "/management/store/detail";
		
	}
	
}
