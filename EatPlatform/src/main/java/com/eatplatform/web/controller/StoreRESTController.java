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
import com.eatplatform.web.domain.StoreAddressVO;
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

	    List<StoreVO> recentStores = storeService.getStoresWithPaging(pageNum, pageSize, keywords);
	    Map<Integer, StoreAddressVO> storeAddressesMap = new HashMap<>();
	    for (StoreVO store : recentStores) {
	        int storeId = store.getStoreId();
	        StoreAddressVO storeAddress = storeAddressService.selectStoreAddressById(storeId);
	        storeAddressesMap.put(storeId, storeAddress); 
	    }
	    
	    int totalStoresCount = storeService.getTotalStoresCount(keywords);
	    int totalPages = (int) Math.ceil((double) totalStoresCount / pageSize);

	    Map<String, Object> response = new HashMap<>();
	    response.put("totalPages", totalPages);
	    response.put("currentPage", pageNum);
	    response.put("recentStores", recentStores);
	    response.put("storeAddresses", storeAddressesMap);
	    response.put("totalStoresCount", totalStoresCount);
	    response.put("keyword", keyword);

	    log.info(recentStores);

	    return response;
	}

}
