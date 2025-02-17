package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.ProductMainCategoryVO;
import com.eatplatform.web.domain.ProductSubCategoryVO;
import com.eatplatform.web.domain.ProductVO;

@Mapper
public interface ProductMapper {
	// 상품 등록
	boolean insertProduct(ProductVO productVO);
	
	// 상위 카테고리 등록
	boolean insertMainCategory(ProductMainCategoryVO productMainCategoryVO);
	
	// 하위 카테고리 등록
	boolean insertSubCategory(ProductSubCategoryVO productSubCategoryVO);

	// 상위 카테고리 조회
	List<ProductMainCategoryVO> selectMainCategoryAll();
	
	// 하위 카테고리 조회
	List<ProductSubCategoryVO> selectSubCategoryByMainCategoroyId(@Param("mainCategoryId") int mainCategoryId);
}
