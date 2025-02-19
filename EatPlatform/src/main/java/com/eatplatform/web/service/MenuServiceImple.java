package com.eatplatform.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.MenuVO;
import com.eatplatform.web.persistence.MenuMapper;
import com.eatplatform.web.persistence.StoreMapper;

import lombok.extern.log4j.Log4j;


@Service
@Log4j
public class MenuServiceImple implements MenuService {
	

	@Autowired
	private MenuMapper menuMapper;

	@Override
	public int registerMenu(MenuVO menuVO) {
		log.info("registerStore()");
		int result = menuMapper.insertMenu(menuVO);
		log.info(menuVO);
		log.info(result + "행 삽입 성공");
		return result;
	}

	@Override
	public int getRepresentMenuCountByStoreId(int storeId) {
		return menuMapper.countRepresentMenuByStoreId(storeId);
	}

	@Override
	public List<MenuVO> selectMenuByStoreId(int storeId) {
		return menuMapper.selectMenuByStoreId(storeId);
	}

	@Override
	public int deleteMenu(int menuId) {
		return menuMapper.deleteMenuByMenuId(menuId);
	}

	@Override
	public String getMenuStoreIdByMenuId(int menuId) {
		return menuMapper.selectMenuStoreIdByMenuId(menuId);
	}

	@Override
	public MenuVO getMenuByMenuId(int menuId) {
		return menuMapper.selectMenuByMenuId(menuId);
	}

}
