package com.eatplatform.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eatplatform.web.service.HolidayService;
import com.eatplatform.web.service.ReservService;
import com.eatplatform.web.service.StoreAddressService;
import com.eatplatform.web.service.StoreService;
import com.eatplatform.web.domain.HolidayVO;
import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.domain.ReservWithStoreNameVO;
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
	
	@Autowired
	private HolidayService holidayService;
	
	@Autowired
	private ReservService reservService;
	

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
	
    /**
     * @param file
     * @return
     */
    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        
        try {
        	String tempPath = "C:\\Users\\tirof\\OneDrive\\Desktop\\Develope\\eatPlatform-springLegacy\\eatPlatform-springLegacy\\EatPlatform\\src\\main\\webapp";
        	String baseUploadDir = tempPath + File.separator +  "upload" + File.separator + "store";
            String year = new SimpleDateFormat("yyyy").format(new Date());
            String month = new SimpleDateFormat("MM").format(new Date());
            String day = new SimpleDateFormat("dd").format(new Date());

            String uploadDir = baseUploadDir + File.separator + year + File.separator + month + File.separator + day + File.separator;

            File folder = new File(uploadDir);
            if (!folder.exists()) {
                boolean created = folder.mkdirs();
                if (!created) {
                	log.info("폴더 못 만드러따");
                	throw new IOException("디렉토리 생성 실패");
                }
                log.info("폴더 만드러따");
            }
            String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            String fileUrl = uploadDir + fileName;
            String datePath = year + File.separator + month + File.separator + day + File.separator + fileName;
            
            Path path = Paths.get(fileUrl);
            Files.write(path, file.getBytes());
            log.info("여기까진 1번");
            response.put("url", datePath);
            log.info("여기까진 2번");

            log.info("uploadDir: " + uploadDir);
            log.info("baseUploadDir: " + baseUploadDir);
            log.info("fileUrl: " + fileUrl);
            log.info("path: " + path);
        } catch (IOException e) {
            response.put("error", "파일 업로드 실패");
            log.info("혹시 실패?");
            return ResponseEntity.status(500).body(response);
        } 
        return ResponseEntity.ok(response);
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
<<<<<<< Updated upstream
    		List<ReservWithStoreNameVO> reservList = reservService.searchReservListByHolidayList(holidayList, storeId);
=======
    		List<ReservVO> reservList = reservService.searchReservListByHolidayList(holidayList, storeId);
>>>>>>> Stashed changes
    		log.info("reservList : " + reservList);
			map.put("reservStatus", 1);
			map.put("reservList", reservList);
			return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
    	} else {
    		map.put("reservStatus", 0);
    	}
    	
    	return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
    }
    
}
