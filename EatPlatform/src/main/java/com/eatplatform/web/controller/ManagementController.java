package com.eatplatform.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatplatform.web.domain.CustomUser;
import com.eatplatform.web.domain.JoinReviewReportVO;
import com.eatplatform.web.domain.JoinStoreApprovalsInfoVO;
import com.eatplatform.web.domain.JoinStoreApprovalsListVO;
import com.eatplatform.web.domain.MenuVO;
import com.eatplatform.web.domain.ReviewReportListWithUserAtNameVO;
import com.eatplatform.web.domain.StoreCategoryVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.persistence.ReviewMapper;
import com.eatplatform.web.service.ManagementService;
import com.eatplatform.web.service.MenuService;
import com.eatplatform.web.service.ReviewReportListService;
import com.eatplatform.web.service.StoreApprovalsService;
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
	
	@Autowired
	private ReviewReportListService reviewReportListService;
	
	@Autowired
	private ReviewMapper reviewMapper;
	
	@Autowired
	private StoreApprovalsService storeApprovalsService;
	
	/**
	 * 가게 목록 조회
	 * @param Model model
	 * @param Pagination pagination
	 * @param CustomUser customUser
	 */
	@GetMapping("/store/list")
	public void managementStoreList(Model model, Pagination pagination, @AuthenticationPrincipal CustomUser customUser) {
		log.info("managementStoreList()");
		String username = customUser.getUsername();
		
		List<StoreVO> storeList = managementService.searchStoreList(pagination, username);
		List<Integer> storeIdList = new ArrayList<>();
		List<StoreCategoryVO> storeCategoryList = new ArrayList<>();
		
		for(int i = 0; i < storeList.size(); i++) {
			storeIdList.add(storeList.get(i).getStoreId());
		}
		
		if(storeList.size() != 0) {
			storeCategoryList = storeService.getStoreCategory(storeIdList);
		}
		
		int totalCount = managementService.getTotalStoresCount(username);
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		pageMaker.setTotalCount(totalCount);
		
		log.info(storeList);
		log.info(storeCategoryList);
		model.addAttribute("list", storeList);
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("categoryList", storeCategoryList);
		
	}

	/**
	 * 가게 상세 정보 조회
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
	
	@GetMapping("/reserv/list")
	public String managementReservList(Model model) {
		return "/management/reserv/list";
	}
	
	/**
	 * @param model
	 * @param pagination
	 * @return
	 */
	@GetMapping("/report/reviewList")
	public String reviewReportList(Model model, Pagination pagination) {
		List<JoinReviewReportVO> reviewReportList = reviewReportListService.getReviewReportList(pagination);
		int totalCount = reviewMapper.getReviewReportCount();
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		pageMaker.setTotalCount(totalCount);
		
		model.addAttribute("list", reviewReportList);
		model.addAttribute("pageMaker", pageMaker);
		
		return "/management/report/reviewList";
	}
	
	// 가게 등록 요청 목록 화면 호출
	@GetMapping("/store/requestList")
	public void requestList(Model model, Pagination pagination) {
		log.info("requestList()");
		List<JoinStoreApprovalsListVO> list = storeApprovalsService.searchList(pagination);
		int totalCount = storeApprovalsService.getTotalCount();
		log.info("totalCount : " + totalCount);
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		pageMaker.setTotalCount(totalCount);
		
		model.addAttribute("list", list);
		model.addAttribute("pageMaker", pageMaker);
	}
	
	// 가게 등록 요청 정보 화면 호출
	@GetMapping("/store/requestInfo")
	public void requestInfo(Model model, @RequestParam("storeId") int storeId, @RequestParam("pageNum") int pageNum) {
		log.info("requestInfo()");
		JoinStoreApprovalsInfoVO infoVO = storeApprovalsService.searchInfo(storeId);
		if(infoVO.getDetailAddress() == null || infoVO.getDetailAddress().isEmpty()) {
			infoVO.setDetailAddress("상세 주소가 없습니다.");
		}
		model.addAttribute("info", infoVO);
		model.addAttribute("pageNum", pageNum);
	}
	
}
