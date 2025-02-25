package com.eatplatform.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.domain.ProductCategoryVO;
import com.eatplatform.web.domain.ProductMainCategoryVO;
import com.eatplatform.web.domain.ProductSubCategoryVO;
import com.eatplatform.web.domain.ProductVO;
import com.eatplatform.web.persistence.ProductMapper;
import com.eatplatform.web.persistence.StoreMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ProductServiceImple implements ProductService {

	@Autowired
	private StoreMapper storeMapper;
	
	@Autowired
	private ProductMapper productMapper;

	// 상품 등록
	@Transactional(value = "transactionManager")
	@Override
	public boolean registerProduct(ProductVO productVO, ProductCategoryVO productCategoryVO) {
		int productStoreId = productVO.getProductStoreId();
		String productStoreName = storeMapper.getStoreNameByStoreId(productStoreId);
		productVO.setProductStoreName(productStoreName);
		boolean resultProduct = productMapper.insertProduct(productVO);
		
		int productId = productVO.getProductId();
		int mainCategoryId = productCategoryVO.getMainCategoryId();
		int subCategoryId = productCategoryVO.getSubCategoryId();
		String mainCategoryName = productMapper.selectMainCategoryNameByMainCategoryId(mainCategoryId);
		String subCategoryName = productMapper.selectSubCategoryNameBySubCategoryId(subCategoryId);
		productCategoryVO.setProductId(productId);
		productCategoryVO.setMainCategoryName(mainCategoryName);
		productCategoryVO.setSubCategoryName(subCategoryName);
		boolean resultProductCategory = productMapper.insertProductCategory(productCategoryVO);
		
		log.info("상품 등록 성공: " + resultProduct);
		log.info("상품 카테고리 등록 성공: " + resultProductCategory);
		return resultProduct;
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

	// 상품 목록 전체 조회
	@Override
	public List<ProductVO> getProductAllList(int pageNum, int pageSize, String keyword) {
		int startNum =(pageNum -1) * pageSize + 1;
		int endNum = pageNum * pageSize;
		
		Map<String, Object> params = new HashMap<>();
		params.put("startNum", startNum);
		params.put("endNum", endNum);
		params.put("keyword", keyword);
		return productMapper.selectProductAllList(params);
	}
	
	// 상품 카테고리 조회
	@Override
	public List<ProductCategoryVO> getProductCategory(List<Integer> productIdList) {
		return productMapper.selectProductCategory(productIdList);
	}
	
	// 상위 카테고리 조회
	@Override
	public List<ProductMainCategoryVO> getMainCategoryAll() {
        return productMapper.selectMainCategoryAll();
	}
	
	// 하위 카테고리 조회 (ByMainCategoryId)
	@Override
	public List<ProductSubCategoryVO> getSubCategoryByMainCategoryId(int mainCategoryId) {
		return productMapper.selectSubCategoryByMainCategoroyId(mainCategoryId);
	}
	
	@Override
	public List<ProductVO> getProductBySellerId(int pageNum, int pageSize, String sellerId) {
		int startNum =(pageNum -1) * pageSize + 1;
		int endNum = pageNum * pageSize;
		
		Map<String, Object> params = new HashMap<>();
		params.put("startNum", startNum);
		params.put("endNum", endNum);
		params.put("sellerId", sellerId);
		return productMapper.selectProductListBySellerId(params);
	}
	
	// 상품 정보 조회 (ByProductId)
	@Override
	public ProductVO getProductByProductId(int productId) {
		return productMapper.selectProductByProductId(productId);
	}

	// 상품 카테고리 조회 (ByProductId)
	@Override
	public ProductCategoryVO getProductCategoryByProductId(int productId) {
		return productMapper.selectProductCategoryByProductId(productId);
	}
	
	// 상위 카테고리 조회 (ByMainCategoryId)
	@Override
	public ProductMainCategoryVO getMainCategoryByMainCategoryId(int mainCategoryId) {		
		return productMapper.selectMainCategoryByMainCategoryId(mainCategoryId);
	}

	// 하위 카테고리 조회 (BySubCategoryId)
	@Override
	public ProductSubCategoryVO getSubCategoryBySubCategoryId(int subCategoryId) {
		return productMapper.selectSubCategoryBySubCategoryId(subCategoryId);
	}

	// 상품 목록 전체 카운트
	@Override
	public int getProductAllListCount(String keyword) {
		return productMapper.selectProductAllListCount(keyword);
	}
	
	// 상품 리스트 조회 카운트
	@Override
	public int getProductListBySellerIdCount(String sellerId) {
		return productMapper.selectProductListBySellerIdCount(sellerId);
	}

	// 상품 정보 수정
	@Transactional(value = "transactionManager")
	@Override
	public boolean modifyProduct(ProductVO productVO, ProductCategoryVO productCategoryVO) {
		int productStoreId = productVO.getProductStoreId();
		String productStoreName = storeMapper.getStoreNameByStoreId(productStoreId);
		productVO.setProductStoreName(productStoreName);
		boolean resultProduct = productMapper.updateProduct(productVO);
		
		int productId = productVO.getProductId();
		int mainCategoryId = productCategoryVO.getMainCategoryId();
		int subCategoryId = productCategoryVO.getSubCategoryId();
		String mainCategoryName = productMapper.selectMainCategoryNameByMainCategoryId(mainCategoryId);
		String subCategoryName = productMapper.selectSubCategoryNameBySubCategoryId(subCategoryId);
		productCategoryVO.setProductId(productId);
		productCategoryVO.setMainCategoryName(mainCategoryName);
		productCategoryVO.setSubCategoryName(subCategoryName);
		boolean resultProductCategory = productMapper.updateProductCategory(productCategoryVO);
		
		log.info("상품 수정 성공: " + resultProduct);
		log.info("상품 카테고리 수정 성공: " + resultProductCategory);
		return resultProduct;
	}

	// 상위 카테고리 수정
	@Override
	public boolean modifyMainCategory(ProductMainCategoryVO productMainCategoryVO) {
		return productMapper.updateMainCategory(productMainCategoryVO);
	}

	// 하위 카테고리 수정
	@Override
	public boolean modifySubCategory(ProductSubCategoryVO productSubCategoryVO) {
		return productMapper.updateSubCategory(productSubCategoryVO);
	}
	
	// 상품 정보 삭제
	@Override
	public boolean deleteProduct(int productId) {
		return productMapper.deleteProduct(productId);
	}

	// 상위 카테고리 삭제
	@Override
	public boolean deleteMainCategory(int mainCategoryId) {
		return productMapper.deleteMainCategory(mainCategoryId);
	}
	
	// 하위 카테고리 삭제
	@Override
	public boolean deleteSubCategory(int subCategoryId) {
		return productMapper.deleteSubCategory(subCategoryId);
	}

}
