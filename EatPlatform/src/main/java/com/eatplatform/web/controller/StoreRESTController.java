package com.eatplatform.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.service.StoreAddressService;
import com.eatplatform.web.service.StoreService;
import com.eatplatform.web.domain.StoreVO;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping(value = "/store")
@Log4j
public class StoreRESTController {

	@Autowired
	private StoreService storeService;

	@Autowired
	private StoreAddressService storeAddressService;

	@GetMapping("/map/list")
	@ResponseBody
	public Map<String, Object> getStoresWithPaging(@RequestParam(defaultValue = "1") int pageNum,
	                                               @RequestParam(value = "keyword", required = false) String keyword) {
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

	    // 최근 데이터 가져오기
	    List<StoreVO> recentStores = storeService.getStoresWithPaging(pageNum, pageSize, keywords);

	    // 전체 데이터 개수 가져오기
	    int totalStoresCount = storeService.getTotalStoresCount(keywords);

	    // 전체 페이지 수 계산
	    int totalPages = (int) Math.ceil((double) totalStoresCount / pageSize);

	    Map<String, Object> response = new HashMap<>();
	    response.put("totalPages", totalPages);         // 전체 페이지 수
	    response.put("currentPage", pageNum);           // 현재 페이지 번호
	    response.put("recentStores", recentStores);    // 현재 페이지에 해당하는 데이터
	    response.put("totalStoresCount", totalStoresCount);  // 전체 데이터 개수
	    response.put("keyword", keyword);               // 검색어

	    log.info(recentStores);

	    return response;
	}

}
