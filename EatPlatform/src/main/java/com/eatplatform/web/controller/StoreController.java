package com.eatplatform.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatplatform.web.domain.MenuVO;
import com.eatplatform.web.domain.StoreAddressVO;
import com.eatplatform.web.domain.StoreCategoryVO;
import com.eatplatform.web.domain.StoreImageVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.service.MenuService;
import com.eatplatform.web.service.StoreAddressService;
import com.eatplatform.web.service.StoreImageService;
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
	private StoreAddressService storeAddressService;

	@Autowired
	private MenuService menuService;

	
	@Autowired
	private StoreImageService storeImageService;

	@GetMapping("/newStore")
	public String newStore(Model model) {
		log.info("newStore()");

		StoreVO storeVO = new StoreVO();

		model.addAttribute("storeVO", storeVO);

		return "/store/newStore";
	}

	@PostMapping("/register")
	public String register(
			StoreVO storeVO, 
			StoreAddressVO storeAddressVO, 
			StoreCategoryVO storeCategoryVO,
			Model model, 
			@AuthenticationPrincipal UserDetails userDetails) {
		String userId = userDetails.getUsername();
		storeVO.setStoreUserId(userId);
		int result = storeService.registerStore(storeVO, storeAddressVO, storeCategoryVO);

		model.addAttribute("result", result);

		return "/store/register";
	}

	@GetMapping("/list")
	public String getStoresWithPaging(@RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(value = "keyword", required = false) String keyword, Model model) {
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
		
		if(recentStores != null && !recentStores.isEmpty()) {
			List<Integer> storeIdList = recentStores.stream().map(StoreVO::getStoreId)
					.collect(Collectors.toList());
			List<StoreCategoryVO> storeCategoryList = storeService.getStoreCategory(storeIdList);
			
			Map<Integer, List<StoreCategoryVO>> categoryMap = storeCategoryList.stream()
					.collect(Collectors.groupingBy(StoreCategoryVO::getStoreId));
			
			List<Map<String, Object>> mergedList = new ArrayList<>();
			
			recentStores.forEach(stores -> {
				int storeId = stores.getStoreId();

				List<StoreImageVO> storeImageList = storeImageService.getImageListByStoreId(storeId);
				stores.setStoreImageList(storeImageList);
				
				List<StoreCategoryVO> categories = categoryMap.getOrDefault(storeId, Collections.emptyList());
				
				categories.forEach(category -> {
					Map<String, Object> mergedStore = new HashMap<>();
					mergedStore.put("storeId", stores.getStoreId());
					mergedStore.put("storeName", stores.getStoreName());
					mergedStore.put("storeComment", stores.getStoreComment());
					mergedStore.put("storePhone", stores.getStorePhone());
					mergedStore.put("businessHour", stores.getBusinessHour());
					mergedStore.put("storeImageList", stores.getStoreImageList());
					mergedStore.put("mainCategoryId", category.getMainCategoryId());
					mergedStore.put("subCategoryId", category.getSubCategoryId());
					mergedStore.put("mainCategoryName", category.getMainCategoryName());
					mergedStore.put("subCategoryName", category.getSubCategoryName());
					
					mergedList.add(mergedStore);
					
				});
			});
			
			model.addAttribute("recentStores", mergedList);
			
		} else {
			model.addAttribute("recentStores", recentStores);
		}
		
		
		int totalStoresCount = storeService.getTotalStoresCount(keywords);
		int totalPages = (int) Math.ceil((double) totalStoresCount / pageSize);

		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("keyword", keyword);
		log.info("RECENTSTORES : " + recentStores);

		return "/store/list";
	}

	@GetMapping("/map")
	public String map() {
		log.info("map()");
		return "/store/map";
	}
	
	@GetMapping("/updateStore")
	public String updateStore(@RequestParam("storeId") int storeId, Model model,
			@AuthenticationPrincipal UserDetails userDetails) {
		StoreVO storeVO = storeService.selectStoreById(storeId);
		StoreAddressVO storeAddressVO = storeAddressService.selectStoreAddressById(storeId);
		StoreCategoryVO storeCategoryVO = storeService.getStoreCategoryByStoreId(storeId);

		String currentUserId = userDetails.getUsername();
		String dbUserId = storeService.getStoreUserIdByStoreId(storeVO.getStoreId());
		log.info("storeVO : " + storeVO);
		log.info("currentUserId : " + currentUserId + "// dbUserId : " + dbUserId);

		if (dbUserId != null && dbUserId.equals(currentUserId)) {
			
			StoreCategoryVO storeCategory = storeService.getStoreCategoryByStoreId(storeId);

			model.addAttribute("storeCategory", storeCategory);
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
			model.addAttribute("storeAddressVO", storeAddressVO);
			model.addAttribute("storeCategoryVO", storeCategoryVO);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
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
	public String modify(
			@ModelAttribute StoreVO storeVO,
			@ModelAttribute StoreAddressVO storeAddressVO,
			@ModelAttribute StoreCategoryVO storeCategoryVO,
			Model model,
			@AuthenticationPrincipal UserDetails userDetails) {
		log.info("modify()");
		log.info(storeVO + "기본 정보");
		log.info(storeAddressVO + "주소");

		String currentUserId = userDetails.getUsername();
		String dbUserId = storeService.getStoreUserIdByStoreId(storeVO.getStoreId());
		if (dbUserId != null && dbUserId.equals(currentUserId)) {
			storeVO.setStoreUserId(dbUserId);

			int result = storeService.modifyStore(storeVO, storeAddressVO, storeCategoryVO);
			if (result == 1) {
				log.info("가게 수정 성공");
			}
			log.info(result);
			model.addAttribute("result", result);
			return "/store/modify";
		} else {
			String errHandler = "otherUser";
			log.info(errHandler);
			log.info("잘못된 User 접근");
			model.addAttribute("errHandler", errHandler);
			return "/store/errHandler";
		}
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
		
		// 이미지
		List<StoreImageVO> storeImageList = storeImageService.getImageListByStoreId(storeId);
		storeVO.setStoreImageList(storeImageList);

		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("storeVO", storeVO);
		model.addAttribute("menuVO", menuVO);
		
		log.info("storeVO : " + storeVO);
		return "/store/detail";
	}
	
}
