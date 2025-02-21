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

	@GetMapping("/newProduct")
	public String newProduct(@AuthenticationPrincipal UserDetails userDetails, StoreVO storeVO, ProductVO productVO,
			ProductCategoryVO productCategoryVO, 
			Model model) {
		log.info("Shop/Product/Register, 상품등록");
		String userId = userDetails.getUsername();
		List<StoreVO> storeList = storeService.getStoreIdByStoreUserId(userId);
		log.info("storeList: " + storeList);
		model.addAttribute("storeList", storeList);

		return "/shop/product/newProduct";
	}

	@PostMapping("/register")
	public String register(@AuthenticationPrincipal UserDetails userDetails, ProductVO productVO,
			ProductCategoryVO productCategoryVO, 
			Model model) {
		String userId = userDetails.getUsername();
		productVO.setSellerId(userId);
		boolean result = productService.registerProduct(productVO, productCategoryVO);

		model.addAttribute("result", result);

		return "/shop/product/register";
	}

	@GetMapping("/category/management")
	public String management() {
		log.info("Shop/Product/Category/Management, management");
		return "/shop/product/category/management";
	}

}
