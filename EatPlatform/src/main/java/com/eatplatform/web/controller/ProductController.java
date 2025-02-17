package com.eatplatform.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eatplatform.web.domain.ProductMainCategoryVO;
import com.eatplatform.web.service.ProductService;

import lombok.extern.log4j.Log4j;
@Controller
@RequestMapping(value = "/shop/product")
@Log4j
public class ProductController {
	
	@Autowired
	private ProductService productService;

	@GetMapping("/register")
	public String register() {
		return "/shop/product/register";
	}

	@GetMapping("/category/management")
	public String management(Model model) {
		log.info("Shop/Product/Category/Management, management");
        List<ProductMainCategoryVO> mainCategories = productService.getMainCategoryAll();
        model.addAttribute("mainCategories", mainCategories);
		return "/shop/product/category/management";
	}
	
	
}
