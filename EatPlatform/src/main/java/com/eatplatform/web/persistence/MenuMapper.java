package com.eatplatform.web.persistence;

import com.eatplatform.web.domain.MenuVO;

public interface MenuMapper {

	// 메뉴 등록
	int insertMenu(MenuVO menuVO);
	
	// 대표 메뉴 카운트
	int countRepresentMenuByStoreId(int storeId);
}
