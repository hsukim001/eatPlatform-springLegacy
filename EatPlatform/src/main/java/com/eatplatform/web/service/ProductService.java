package com.eatplatform.web.service;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.ProductMainCategoryVO;
import com.eatplatform.web.domain.ProductSubCategoryVO;
import com.eatplatform.web.domain.ProductVO;

public interface ProductService {
	// 상품 등록
	boolean registerProduct(ProductVO productVO);

	// 상위 카테고리 등록
	boolean registerMainCategroy(ProductMainCategoryVO productMainCategoryVO);
	
	// 하위 카테고리 등록
	boolean registerSubCategory(ProductSubCategoryVO productSubCategoryVO);
	
	// 상위 카테고리 조회
	List<ProductMainCategoryVO> getMainCategoryAll();

	// 하위 카테고리 조회
	List<ProductSubCategoryVO> getSubCategoryByMainCategoryId(@Param("mainCategoryId") int mainCategoryId);
}
