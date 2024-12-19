package com.eatplatform.web.service;

import com.eatplatform.web.domain.MenuVO;

public interface MenuService {
	
	// 메뉴 등록
	int registerMenu(MenuVO menuVO);
	
	// 대표 메뉴 카운트
	int getRepresentMenuCountByStoreId(int storeId);
}
