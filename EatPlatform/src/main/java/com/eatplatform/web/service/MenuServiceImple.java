package com.eatplatform.web.service;

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

}
