package com.eatplatform.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.ProductMainCategoryVO;
import com.eatplatform.web.domain.ProductSubCategoryVO;
import com.eatplatform.web.domain.ProductVO;
import com.eatplatform.web.persistence.ProductMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ProductServiceImple implements ProductService {

	@Autowired
	private ProductMapper productMapper;

	// 상품 등록
	@Override
	public boolean registerProduct(ProductVO productVO) {
		// TODO Auto-generated method stub
		return true;
	}

	// 상위 카테고리 등록
	@Override
	public boolean registerMainCategroy(ProductMainCategoryVO productMainCategoryVO) {
		String categoryName = productMainCategoryVO.getMainCategoryName();
		boolean result = productMapper.insertMainCategory(productMainCategoryVO);
		log.info("등록된 메인 카테고리명: " + categoryName);
		return result;
	}

	// 하위 카테고리 등록
	@Override
	public boolean registerSubCategory(ProductSubCategoryVO productSubCategoryVO) {
		String categoryName = productSubCategoryVO.getSubCategoryName();
		boolean result = productMapper.insertSubCategory(productSubCategoryVO);
		
		log.info("하위 카테고리 등록 성공");
		log.info("상위 카테고리 ID: " + productSubCategoryVO);
		log.info("등록된 하위 카테고리명: " + categoryName);
		return result;
	}
	
	// 상위 카테고리 조회
	@Override
	public List<ProductMainCategoryVO> getMainCategoryAll() {
        return productMapper.selectMainCategoryAll();
	}

	// 하위 카테고리 조회
	@Override
	public List<ProductSubCategoryVO> getSubCategoryByMainCategoryId(int mainCategoryId) {
		return productMapper.selectSubCategoryByMainCategoroyId(mainCategoryId);
	}
}
