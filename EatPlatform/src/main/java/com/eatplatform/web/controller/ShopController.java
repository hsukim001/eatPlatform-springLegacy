package com.eatplatform.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatplatform.web.domain.ProductCategoryVO;
import com.eatplatform.web.domain.ProductVO;
import com.eatplatform.web.service.ProductService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping(value = "/shop")
@Log4j
public class ShopController {

	@Autowired
	ProductService productService;

	@GetMapping("/")
	public String root() {
		log.info("Shop, root");

		return "/shop/list";
	}	

	@GetMapping("/list")
	public String list() {
		log.info("Shop, list(), 상품 리스트");

		return "/shop/list";
	}

	@GetMapping("/detail")
	public String detail(
			@RequestParam("productId") int productId,
			Model model) {
		log.info("Shop, detail(), 상품 상세 페이지");
		
		ProductVO productVO = productService.getProductByProductId(productId);
		ProductCategoryVO productCategoryVO = productService.getProductCategoryByProductId(productId);
		
		model.addAttribute("product", productVO);
		model.addAttribute("productCategory", productCategoryVO);
		return "/shop/detail";
	}
}
