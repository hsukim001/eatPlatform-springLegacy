package com.eatplatform.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.ProductCategoryVO;
import com.eatplatform.web.domain.ProductVO;
import com.eatplatform.web.service.ProductService;
import com.eatplatform.web.service.UserService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping(value = "/shop")
@Log4j
public class ShopRestController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;

	// 상품 목록 전체 조회
	@GetMapping("/list/all")
	public ResponseEntity<Map<String, Object>> viewProductList(@RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(value = "keyword", required = false) String keyword) {
		Map<String, Object> response = new HashMap<>();

		if (pageNum <= 0) {
			pageNum = 1;
		}
		int pageSize = 10;

		List<ProductVO> productList = productService.getProductAllList(pageNum, pageSize, keyword);
		if (productList != null && !productList.isEmpty()) {
			List<Integer> productIdList = productList.stream().map(ProductVO::getProductId)
					.collect(Collectors.toList());
			List<ProductCategoryVO> productCategoryList = productService.getProductCategory(productIdList);

			Map<Integer, List<ProductCategoryVO>> categoryMap = productCategoryList.stream()
					.collect(Collectors.groupingBy(ProductCategoryVO::getProductId));

			List<Map<String, Object>> mergedList = new ArrayList<>();
			

			productList.forEach(product -> {
				int productId = product.getProductId();
				List<ProductCategoryVO> categories = categoryMap.getOrDefault(productId, Collections.emptyList());

				categories.forEach(category -> {

					String sellerId = product.getSellerId();
					String sellerName = userService.getStoreUserNameByUserId(sellerId);
					
					Map<String, Object> mergedProduct = new HashMap<>();
					mergedProduct.put("productId", product.getProductId());
					mergedProduct.put("productStoreId", product.getProductStoreId());
					mergedProduct.put("productStoreName", product.getProductStoreName());
					mergedProduct.put("sellerName", sellerName);
					mergedProduct.put("productName", product.getProductName());
					mergedProduct.put("productPrice", product.getProductPrice());
					mergedProduct.put("productBundle", product.getProductBundle());
					mergedProduct.put("productStock", product.getProductStock());
					mergedProduct.put("mainCategoryName", category.getMainCategoryName());
					mergedProduct.put("subCategoryName", category.getSubCategoryName());
					mergedProduct.put("mainCategoryId", category.getMainCategoryId());
					mergedProduct.put("subCategoryId", category.getSubCategoryId());

					mergedList.add(mergedProduct);
				});
			});

			log.info("Merged Product List: " + mergedList);

			response.put("mergedList", mergedList);
		}

		int totalCount = productService.getProductAllListCount(keyword);
		response.put("totalCount", totalCount);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
