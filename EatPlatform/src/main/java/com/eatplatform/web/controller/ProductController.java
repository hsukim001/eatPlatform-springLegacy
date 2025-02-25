package com.eatplatform.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatplatform.web.domain.ProductCategoryVO;
import com.eatplatform.web.domain.ProductVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.service.ProductService;
import com.eatplatform.web.service.StoreService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping(value = "/shop/product")
@Log4j
public class ProductController {

	@Autowired
	private StoreService storeService;

	@Autowired
	private ProductService productService;

	// 상품 등록 데이터 입력
	@GetMapping("/newProduct")
	public String newProduct(
			@AuthenticationPrincipal UserDetails userDetails, 
			StoreVO storeVO, 
			ProductVO productVO,
			ProductCategoryVO productCategoryVO, 
			Model model) {
		log.info("Shop/Product/Register, 상품등록");
		String userId = userDetails.getUsername();
		List<StoreVO> storeList = storeService.getStoreIdByStoreUserId(userId);
		log.info("storeList: " + storeList);
		model.addAttribute("storeList", storeList);

		return "/shop/product/newProduct";
	}

	// 상품 등록
	@PostMapping("/register")
	public String register(
			@AuthenticationPrincipal UserDetails userDetails, 
			ProductVO productVO,
			ProductCategoryVO productCategoryVO, 
			Model model) {
		String userId = userDetails.getUsername();
		productVO.setSellerId(userId);
		boolean result = productService.registerProduct(productVO, productCategoryVO);

		model.addAttribute("result", result);

		return "/shop/product/register";
	}

	// 카테고리 관리
	@GetMapping("/category/management")
	public String categoryManagement() {
		log.info("Shop/Product/Category/Management, 카테고리 관리");
		return "/shop/product/category/management";
	}
	
	// 상품 관리
	@GetMapping("/management")
	public String management() {
		log.info("Shop/Product/Management, 상품 관리");
		
		return "/shop/product/management";
	}
	
	// 상품 데이터 수정
	@GetMapping("/updateProduct")
	public String updateProduct(
			@RequestParam int productId,
			ProductVO productVO,
			ProductCategoryVO productCategoryVO, 
			Model model,
			@AuthenticationPrincipal UserDetails userDetails) {
		log.info("Shop/Product/UpdateProduct, 상품 수정");

		String userId = userDetails.getUsername();
		List<StoreVO> storeList = storeService.getStoreIdByStoreUserId(userId);
		ProductVO product = productService.getProductByProductId(productId);
		ProductCategoryVO productCategory = productService.getProductCategoryByProductId(productId);
		
		model.addAttribute("storeList", storeList);
		model.addAttribute("product", product);
		model.addAttribute("productCategory", productCategory);
		return "/shop/product/updateProduct";
	}
	
	@PostMapping("/modify")
	public String modify(
			ProductVO productVO,
			ProductCategoryVO productCategoryVO, 
			Model model) {
		log.info("Shop/Product/Modify, 상품 정보 수정");
		
		boolean result = productService.modifyProduct(productVO, productCategoryVO);

		model.addAttribute("result", result);
		return "/shop/product/modify";
	}
}
