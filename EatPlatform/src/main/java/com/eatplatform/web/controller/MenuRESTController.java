package com.eatplatform.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.CustomUser;
import com.eatplatform.web.domain.MenuVO;
import com.eatplatform.web.service.MenuService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/store/menu")
@Log4j
public class MenuRESTController {
	
	@Autowired
	private MenuService menuService;
	
	/**
	 * @param menuId
	 * @return
	 */
	@GetMapping("/search/menu/{menuId}")
	public ResponseEntity<MenuVO> searchMenu(@PathVariable("menuId") int menuId, @AuthenticationPrincipal CustomUser customUser) {
		log.info("searchMenu()");
		
		MenuVO menuVO = new MenuVO();
		int check = menuService.checkCreatedUserandModifyUser(customUser.getUsername(), menuId);
		
		if(check == 1) {
			menuVO = menuService.getMenuByMenuId(menuId);			
		} else {
			return new ResponseEntity<MenuVO>(menuVO, HttpStatus.BAD_REQUEST);
		}
		
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
	 * @param MenuVO menuVO
	 * @param CustomUser customUser
	 * @return ResponseEntity<Integer>
	 */
	@PutMapping("/update")
	public ResponseEntity<Integer> updateMenu(@RequestBody MenuVO menuVO, @AuthenticationPrincipal CustomUser customUser) {
		log.info("updateMenu()");
		log.info("menuVO : " + menuVO);
		
		String createdUsername = menuService.getMenuStoreIdByMenuId(menuVO.getMenuId());
		log.info("menuId : " + menuVO.getMenuId());
		log.info("createdUsername : " + createdUsername);
		int result = 0;
		
		if(createdUsername.equals(customUser.getUsername()) && createdUsername != null) {
			result = menuService.updateMenu(menuVO);			
		}
		
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
		String modifyUsername = customUser.getUsername();
		String createdUsername = menuService.getMenuStoreIdByMenuId(menuId);
		log.info("createdUsername : " + createdUsername);
		log.info("modifyUsername : " + modifyUsername);
		
		if(createdUsername.equals(modifyUsername) && createdUsername != null) {
			result = menuService.deleteMenu(menuId);			
		}
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
}
