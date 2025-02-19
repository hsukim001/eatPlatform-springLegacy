package com.eatplatform.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.CustomUser;
import com.eatplatform.web.domain.MenuVO;
import com.eatplatform.web.service.MenuService;
import com.eatplatform.web.service.StoreService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/store/menu")
@Log4j
public class MenuRESTController {
	
	@Autowired
	private MenuService menuService;
	
	@GetMapping("/search/menu/{menuId}")
	public ResponseEntity<MenuVO> searchMenu(@PathVariable("menuId") int menuId) {
		MenuVO menuVO = menuService.getMenuByMenuId(menuId);
		return new ResponseEntity<MenuVO>(menuVO, HttpStatus.OK);
	}
	
	/**
	 * @param menuVO
	 * @return
	 */
	@PostMapping("/created")
	public ResponseEntity<Integer> createdMenu(@RequestBody MenuVO menuVO) {
		int result = menuService.registerMenu(menuVO);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	/**
	 * @param int menuId
	 * @return ResponseEntitiy<Integer>
	 */
	@DeleteMapping("/delete/{menuId}")
	public ResponseEntity<Integer> deleteMenu(@PathVariable("menuId") int menuId, @AuthenticationPrincipal CustomUser customUser) {
		log.info("deleteMenu");
		
		int result = 0;
		String currentUserId = customUser.getUsername();
		String dbStoreUserId = menuService.getMenuStoreIdByMenuId(menuId);
		log.info("dbStoreUserId : " + dbStoreUserId);
		log.info("currentUserId : " + currentUserId);
		
		if(dbStoreUserId.equals(currentUserId) && dbStoreUserId != null) {
			result = menuService.deleteMenu(menuId);			
		}
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
}
