package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.MenuVO;

public interface MenuService {
	
	// 메뉴 등록
	int registerMenu(MenuVO menuVO);
	
	// 대표 메뉴 카운트
	int getRepresentMenuCountByStoreId(int storeId);

	// storeId로 검색하여 조회
	List<MenuVO> selectMenuByStoreId(int storeId);
	
	/**
	 * @param menuId
	 * @return
	 */
	MenuVO getMenuByMenuId(int menuId);
	
	/**
	 * @param MenuId
	 * @return
	 */
	String getMenuStoreIdByMenuId(int MenuId);
	
	/**
	 * @param modifyUsername
	 * @param menuId
	 * @return
	 */
	int checkCreatedUserandModifyUser(String modifyUsername, int menuId);
	
	/**
	 * @param MenuVO menuVO
	 * @return int
	 */
	int updateMenu(MenuVO menuVO);
	
	/**
	 * @param int menuId
	 * @return int
	 */
	int deleteMenu(int menuId);
}
