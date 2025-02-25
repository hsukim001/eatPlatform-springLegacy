package com.eatplatform.web.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.ProductCategoryVO;
import com.eatplatform.web.domain.ProductMainCategoryVO;
import com.eatplatform.web.domain.ProductSubCategoryVO;
import com.eatplatform.web.domain.ProductVO;

@Mapper
public interface ProductMapper {
	// 상품 등록
	boolean insertProduct(ProductVO productVO);
	
	// 상품 카테고리 등록
	boolean insertProductCategory(ProductCategoryVO productCategoryVO);
	
	// 상위 카테고리 등록
	boolean insertMainCategory(ProductMainCategoryVO productMainCategoryVO);
	
	// 하위 카테고리 등록
	boolean insertSubCategory(ProductSubCategoryVO productSubCategoryVO);

	// 상품 목록 전체 조회
	List<ProductVO> selectProductAllList(Map<String, Object> params);
	
	// 상품 카테고리 조회
	List<ProductCategoryVO> selectProductCategory(@Param("productIdList") List<Integer> productIdList);
	
	// 상위 카테고리 조회
	List<ProductMainCategoryVO> selectMainCategoryAll();
	
	// 하위 카테고리 조회 (ByMainCategoryId)
	List<ProductSubCategoryVO> selectSubCategoryByMainCategoroyId(@Param("mainCategoryId") int mainCategoryId);
	
	// 상품 리스트 조회(BySellerName) 
	List<ProductVO> selectProductListBySellerId(Map<String, Object> params);
	
	// 상품 정보 조회(ByProductId)
	ProductVO selectProductByProductId(@Param("productId") int productId);
	
	// 상품 카테고리 조회 (ByProductId)
	ProductCategoryVO selectProductCategoryByProductId(@Param("productId") int productId);
	
	// 상위 카테고리 조회 (ByMainCategoryId)
	ProductMainCategoryVO selectMainCategoryByMainCategoryId(@Param("mainCategoryId") int mainCategoryId);
	
	// 하위 카테고리 조회 (BySubCategoryId)
	ProductSubCategoryVO selectSubCategoryBySubCategoryId(@Param("subCategoryId") int subCategoryId);

	// 상품 목록 전체 카운트
	int selectProductAllListCount(@Param("keyword") String keyword);
	
	// 상품 리스트 조회 카운트
	int selectProductListBySellerIdCount(@Param("sellerId") String sellerId);
	
	// 메인 카테고리 이름 검색
	String selectMainCategoryNameByMainCategoryId(@Param("mainCategoryId") int mainCategoryId);
	
	// 서브 카테고리 이름 검색
	String selectSubCategoryNameBySubCategoryId(@Param("subCategoryId") int subCategoryId);
	
	// 상품 정보 수정
	boolean updateProduct(ProductVO prodcutVO);
	
	// 상품 카테고리 수정
	boolean updateProductCategory(ProductCategoryVO productCategoryVO);
	
	// 상위 카테고리 수정
	boolean updateMainCategory(ProductMainCategoryVO productMainCategoryVO);
	
	// 하위 카테고리 수정
	boolean updateSubCategory(ProductSubCategoryVO productSubCategoryVO);
	
	// 상품 정보 삭제
	boolean deleteProduct(@Param("productId") int productId);
	
	// 상위 카테고리 삭제
	boolean deleteMainCategory(@Param("mainCategoryId") int mainCategoryId);

	// 하위 카테고리 삭제
	boolean deleteSubCategory(@Param("subCategoryId") int subCategoryId);
}
