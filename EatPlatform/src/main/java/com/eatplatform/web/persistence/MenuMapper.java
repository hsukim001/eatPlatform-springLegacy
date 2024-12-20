package com.eatplatform.web.persistence;

import java.util.List;

import com.eatplatform.web.domain.MenuVO;

public interface MenuMapper {

	// 메뉴 등록
	int insertMenu(MenuVO menuVO);
	
	// 대표 메뉴 카운트
	int countRepresentMenuByStoreId(int storeId);
	
	// storeId로 검색하여 조회
	List<MenuVO> selectMenuByStoreId(int storeId);
}
