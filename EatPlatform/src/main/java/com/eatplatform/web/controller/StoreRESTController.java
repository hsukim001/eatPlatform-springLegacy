package com.eatplatform.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.service.HolidayService;
import com.eatplatform.web.service.ReservService;
import com.eatplatform.web.service.StoreAddressService;
import com.eatplatform.web.service.StoreImageService;
import com.eatplatform.web.service.StoreService;
import com.eatplatform.web.domain.CustomUser;
import com.eatplatform.web.domain.HolidayVO;
import com.eatplatform.web.domain.ReservWithStoreNameVO;
import com.eatplatform.web.domain.StoreAddressVO;
import com.eatplatform.web.domain.StoreCategoryVO;
import com.eatplatform.web.domain.StoreImageVO;
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
	
	@Autowired
	private HolidayService holidayService;
	
	@Autowired
	private ReservService reservService;	
	
	@Autowired
	private StoreImageService storeImageService;


	/**
	 * @param pageNum
	 * @param keyword
	 * @return
	 */
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
	    Map<Integer, StoreCategoryVO> storeCategoryMap = new HashMap<>();
	    for (StoreVO store : recentStores) {
	        int storeId = store.getStoreId();
	        StoreAddressVO storeAddress = storeAddressService.selectStoreAddressById(storeId);
	        StoreCategoryVO storeCategory = storeService.getStoreCategoryByStoreId(storeId);
	        storeCategoryMap.put(storeId, storeCategory); 
	        storeAddressesMap.put(storeId, storeAddress); 
	    }
	    
	    int totalStoresCount = storeService.getTotalStoresCount(keywords);
	    int totalPages = (int) Math.ceil((double) totalStoresCount / pageSize);

	    Map<String, Object> response = new HashMap<>();
	    response.put("totalPages", totalPages);
	    response.put("currentPage", pageNum);
	    response.put("recentStores", recentStores);
	    response.put("storeCategory", storeCategoryMap);
	    response.put("storeAddresses", storeAddressesMap);
	    response.put("totalStoresCount", totalStoresCount);
	    response.put("keyword", keyword);

	    log.info(recentStores);

	    return response;
	}

    @GetMapping("/holiday/search/list/{storeId}")
    public ResponseEntity<Map<String, Object>> searchHolidayList(@PathVariable("storeId") int storeId) {
    	log.info("searchHolidayList()");
    	
    	Map<String, Object> response = new HashMap<>();
    	List<HolidayVO> holidayList = holidayService.searchHolidayList(storeId);
    	log.info(holidayList);
    	response.put("holidayList", holidayList);
    	return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
    }
    
    /**
     * @param List<HolidayVO> list
     * @return Map<String, Object>
     */
    @PostMapping("/holiday/registration")
    public ResponseEntity<Map<String, Object>> createdHoliday(@RequestBody List<HolidayVO> list) {
    	log.info("createdHoliday()");
    	log.info(list.size());
    	log.info(list);
    	
    	int result = 0;
    	HolidayVO holidayVO = new HolidayVO();
    	Map<String, Object> map = new HashMap<>();
    	
    	if(list.size() > 1 && list.size() <= 20) {
    		result = holidayService.registrationHolidayList(list);
    		map.put("result", result);
    		map.put("reservStatus", 0);
    		
    	} else if(list.size() == 1) {
    		holidayVO.setStoreId(list.get(0).getStoreId());
    		holidayVO.setHoliday(list.get(0).getHoliday());
    		result = holidayService.createdHoliday(holidayVO);
    		map.put("result", result);
			map.put("reservStatus", 0);
    		
    	}
    	return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    
    @DeleteMapping("/holiday/delete")
    public ResponseEntity<Map<String, Object>> deleteHoliday(@RequestBody List<HolidayVO> list) {
    	log.info("deleteHoliday()");
    	log.info(list.size());
    	log.info(list);
    	
    	Map<String, Object> map = new HashMap<>();
    	int result = 0;
    	
    	if(list.size() > 0 && list.size() <= 20) {
    		log.info("if check");
    		result = holidayService.deleteHoliday(list);
    		map.put("result", result);
    		map.put("reservStatus", 0);
    	}
    	
    	return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    
    @GetMapping("/holiday/check/reservList/{storeId}/{array}")
    public ResponseEntity<Map<String, Object>> checkReservList(@PathVariable("storeId") int storeId, @PathVariable("array") String array) {
    	log.info(array);
    	Map<String, Object> map = new HashMap<>();
    	List<HolidayVO> holidayList = new ArrayList<>();
    	String[] holidayArray = array.split(",");
    	
    	for(String holiday : holidayArray) {
    		HolidayVO holidayVO = new HolidayVO();
    		holidayVO.setHoliday(holiday.trim());
    		holidayList.add(holidayVO);
    	}
    	
    	boolean isReservStatus = holidayService.isReservStatus(holidayList, storeId);
    	log.info("isReservStatus : " + isReservStatus);
    	if(isReservStatus) {
    		List<ReservWithStoreNameVO> reservList = reservService.searchReservListByHolidayList(holidayList, storeId);
    		log.info("reservList : " + reservList);
			map.put("reservStatus", 1);
			map.put("reservList", reservList);
			return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
    	} else {
    		map.put("reservStatus", 0);
    	}
    	
    	return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{storeId}")
    public ResponseEntity<Integer> deleteStore(@PathVariable("storeId") int storeId, @AuthenticationPrincipal CustomUser customUser) {
    	int result = 0;
    	String currentUserId = customUser.getUsername();
		String dbUserId = storeService.getStoreUserIdByStoreId(storeId);
		if (dbUserId != null && dbUserId.equals(currentUserId)) {
			result = storeService.deleteStore(storeId);
		}
    	return new ResponseEntity<Integer>(result, HttpStatus.OK);
    }
    
    @GetMapping("/score")
    public ResponseEntity<List<StoreVO>> getStoreListByScore() {
    	List<StoreVO> stores = storeService.getStoresByScore();
    	
    	for (StoreVO store : stores) {
            if (store.getStoreImageList() != null && !store.getStoreImageList().isEmpty()) {
                StoreImageVO firstImage = store.getStoreImageList().get(0);
                store.setStoreImageVO(firstImage);
            }
    	}
    	return ResponseEntity.ok(stores);
    }
    
}
