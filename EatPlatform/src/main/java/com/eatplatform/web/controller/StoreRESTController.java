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
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        
        
	

}
