package com.eatplatform.web.service;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.ProductCategoryVO;
import com.eatplatform.web.domain.ProductMainCategoryVO;
import com.eatplatform.web.domain.ProductSubCategoryVO;
import com.eatplatform.web.domain.ProductVO;

public interface ProductService {
	// 상품 등록
	boolean registerProduct(ProductVO productVO, ProductCategoryVO productCategoryVO);

	// 상위 카테고리 등록
	boolean registerMainCategroy(ProductMainCategoryVO productMainCategoryVO);
	
	// 하위 카테고리 등록
	boolean registerSubCategory(ProductSubCategoryVO productSubCategoryVO);

	// 상품 목록 전체 조회
	List<ProductVO> getProductAllList(int pageNum, int pageSize, String keyword);
	
	// 상품 카테고리 조회
	List<ProductCategoryVO> getProductCategory(@Param("productIdList") List<Integer> productIdList);
		
	// 상위 카테고리 조회
	List<ProductMainCategoryVO> getMainCategoryAll();

	// 하위 카테고리 조회 (ByMainCategoryId)
	List<ProductSubCategoryVO> getSubCategoryByMainCategoryId(@Param("mainCategoryId") int mainCategoryId);
	
	// 상위 카테고리 조회 (ByMainCategoryId)
	ProductMainCategoryVO getMainCategoryByMainCategoryId(@Param("mainCategoryId") int mainCategoryId);
	
	// 하위 카테고리 조회 (BySubCategoryId)
	ProductSubCategoryVO getSubCategoryBySubCategoryId(@Param("subCategoryId") int subCategoryId);

	int getProductAllListCount(@Param("keyword") String keyword);
	
	// 상위 카테고리 수정
	boolean modifyMainCategory(ProductMainCategoryVO productMainCategoryVO);
	
	// 하위 카테고리 수정
	boolean modifySubCategory(ProductSubCategoryVO productSubCategoryVO);
	
	// 상위 카테고리 삭제
	boolean deleteMainCategory(@Param("mainCategoryId") int mainCategoryId);
	
	// 상위 카테고리 삭제
	boolean deleteSubCategory(@Param("subCategoryId") int subCategoryId);
}
