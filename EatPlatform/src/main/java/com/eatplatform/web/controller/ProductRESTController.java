package com.eatplatform.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.ProductMainCategoryVO;
import com.eatplatform.web.domain.ProductSubCategoryVO;
import com.eatplatform.web.service.ProductService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping(value = "/shop/product")
@Log4j
public class ProductRESTController {

	@Autowired
	private ProductService productService;

	// 상위 카테고리 등록
	@PostMapping("/category/mainRegister")
	public ResponseEntity<Map<String, Object>> registerMainCategory(
			@RequestBody ProductMainCategoryVO productMainCategoryVO) {
        Map<String, Object> response = new HashMap<>();
		boolean result = productService.registerMainCategroy(productMainCategoryVO);
		String msg = "";
		
		if (result) {
			msg = "신규 카테고리 등록이 완료되었습니다.";
		} else {
			msg = "신규 카테고리 등록에 실패했습니다. 다시 시도해주세요.";
		}
		response.put("result", result);
        response.put("msg", msg);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	// 하위 카테고리 등록
	@PostMapping("/category/subRegister")
	public ResponseEntity<Map<String, Object>> registerSubCategory(
			@RequestBody ProductSubCategoryVO productSubCategoryVO) {
		Map<String, Object> response = new HashMap<>();
		boolean result = productService.registerSubCategory(productSubCategoryVO);
		String msg = "";
		
		if (result) {
			msg = "신규 카테고리 등록이 완료되었습니다.";
		} else {
			msg = "신규 카테고리 등록에 실패했습니다. 다시 시도해주세요?";
		}
		response.put("result", result);
        response.put("msg", msg);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/category/subView/{mainCategoryId}")
	public ResponseEntity<Map<String, Object>> viewSubCategory(
			@PathVariable("mainCategoryId") Integer mainCategoryId) {
		Map<String, Object> response = new HashMap<>();
		List<ProductSubCategoryVO> categoryList = productService.getSubCategoryByMainCategoryId(mainCategoryId);
		if (categoryList != null && !categoryList.isEmpty()) {
	        response.put("result", true);
	        response.put("data", categoryList);
	        response.put("msg", "서브 카테고리 조회 성공");
	    } else {
	        response.put("result", false);
	        response.put("msg", "해당하는 서브 카테고리가 없습니다.");
	    }
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
