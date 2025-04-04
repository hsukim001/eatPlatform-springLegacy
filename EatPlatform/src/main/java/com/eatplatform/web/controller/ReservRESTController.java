package com.eatplatform.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.CustomUser;
import com.eatplatform.web.domain.HolidayVO;
import com.eatplatform.web.domain.JoinReservUserNameVO;
import com.eatplatform.web.domain.ReservCancelVO;
import com.eatplatform.web.domain.ReservInfoVO;
import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.domain.CancelReservInfoVO;
import com.eatplatform.web.domain.ReservWithStoreNameVO;
import com.eatplatform.web.domain.StoreScheduleVO;
import com.eatplatform.web.service.ReservCancelService;
import com.eatplatform.web.service.ReservService;
import com.eatplatform.web.util.DataResponse;
import com.eatplatform.web.util.PageMaker;
import com.eatplatform.web.util.Pagination;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping(value = "/reserv")
@Log4j
public class ReservRESTController {

	@Autowired
	private ReservService reservService;
	
	@Autowired
	private ReservCancelService reservCancelService;

	// 페이징 예약 목록 조회(pageNum, userId)
	/**
	 * @param pageNum
	 * @param customUser
	 * @return
	 */
	@GetMapping("/toDay/{pageNum}/{type}")
	public ResponseEntity<DataResponse> getReservList(@PathVariable("pageNum") int pageNum, @PathVariable("type") String type, @RequestParam(value = "keyword", required = false, defaultValue="") String keyword, @AuthenticationPrincipal CustomUser customUser) {
		log.info("getReservList()");
		int pageSize = 10;
		
		// session에서 사용자 아이디 로드
		int userId = customUser.getUser().getUserId();
		String username = customUser.getUsername();
		String auth = customUser.getAuthorities().toString();

		Pagination pagination = new Pagination(userId, pageNum, pageSize, keyword, type);
		log.info(pagination);
		
		List<ReservWithStoreNameVO> list = reservService.getReservWithStoreNameList(pagination, auth, username);
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		int totalCount = reservService.getTotalCount(userId, auth, keyword, username, type);
		log.info("totalCount : " + totalCount);
		pageMaker.setTotalCount(totalCount);

		DataResponse dataResponse = new DataResponse(list, pageMaker);

		return new ResponseEntity<DataResponse>(dataResponse, HttpStatus.OK);
	}
	
	@GetMapping("/search/reservInfo/{reservId}")
	public ResponseEntity<Map<String, Object>> searchReservInfo(@PathVariable("reservId") int reservId, @AuthenticationPrincipal CustomUser customUser) {
		Map<String, Object> map = new HashMap<>();
		
		int userId = customUser.getUser().getUserId();
		boolean isUser = reservService.isReservUser(reservId, userId);
		
		ReservInfoVO reservInfoVO = new ReservInfoVO();
		List<String> selectOptionList = new ArrayList<>();
		String storeDetailUrl = "";
		
		if(isUser || customUser.getAuthorities().toString().contains("ROLE_STORE")) {
			reservInfoVO = reservService.getReservInfoByReservId(reservId);
			map.put("info", reservInfoVO);
			
			if(reservInfoVO.getCancelStatus() == 1) {
				ReservCancelVO reservCancelVO = reservCancelService.getReservCancel(reservId);
				map.put("cancelComment", reservCancelVO.getCancelComment());				
			}
			
		} else {
			return new ResponseEntity<Map<String,Object>>(map, HttpStatus.BAD_REQUEST);
		}
		
		if(customUser.getAuthorities().toString().contains("ROLE_MEMBER")) {
			selectOptionList.add("예약 취소사유를 선택해주세요.");
			selectOptionList.add("개인적인 사유");
			selectOptionList.add("건강상의 문제");
			storeDetailUrl = "/store/detail?storeId=" + reservInfoVO.getStoreId();
		} else if(customUser.getAuthorities().toString().contains("ROLE_STORE")) {
			selectOptionList.add("예약 취소사유를 선택해주세요.");
			selectOptionList.add("휴게 시간으로 인하여 취소합니다.");
			selectOptionList.add("휴무일로 인하여 취소합니다.");
			storeDetailUrl = "/management/store/detail?storeId=" + reservInfoVO.getStoreId();
		}
		
		map.put("storeDetailUrl", storeDetailUrl);
		map.put("selectOptionList", selectOptionList);
		
		return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
	}

	// 예약 등록
	/**
	 * @param reservVO
	 * @param customUser
	 * @param reservLimit
	 * @return
	 */
	@PostMapping("/created/{reservLimit}")
	public ResponseEntity<Integer> createdReserv(@RequestBody ReservVO reservVO, @AuthenticationPrincipal CustomUser customUser, @PathVariable("reservLimit") int reservLimit) {
		log.info("createdReserv()");
		ReservVO vo = reservVO;
		
		int userId = customUser.getUser().getUserId();
		vo.setUserId(userId);
		int result = reservService.createdReserv(vo, reservLimit);
		
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	// 예약 가능시간 조회
	/**
	 * @param storeId
	 * @param reservLimit
	 * @param personnel
	 * @param reservDate
	 * @return List<StoreScheduleVO>
	 */
	@GetMapping("/schedule/{storeId}/{reservLimit}/{personnel}/{reservDate}")
	public ResponseEntity<List<StoreScheduleVO>> searchSchedule(@PathVariable("storeId") int storeId, @PathVariable("reservLimit") int reservLimit, @PathVariable("personnel") int personnel, @PathVariable("reservDate") String reservDate) {
		log.info("searchSchedule()");
		
		StoreScheduleVO vo = new StoreScheduleVO();
		vo.setStoreId(storeId);
		vo.setReservLimit(reservLimit);
		vo.setReservDate(reservDate);
		List<StoreScheduleVO> list = reservService.searchSchedule(vo, personnel);
		for(int i = 0; i < list.size(); i++) {
			log.info(list.get(i).getReservDate());
		}
		
		return new ResponseEntity<List<StoreScheduleVO>>(list, HttpStatus.OK);
	}
	
	/**
	 * @param storeId
	 * @param reservDate
	 * @return Map<String, Object>
	 */
	@GetMapping("/choiceDate/{storeId}/{reservDate}")
	public ResponseEntity<Map<String, Object>> choiceDateReserv(@PathVariable("storeId") int storeId, @PathVariable("reservDate") String reservDate) {
		log.info("choiceDateReserv()");
		Map<String, Object> map = new HashMap<>();
		log.info(reservDate);
		List<StoreScheduleVO> reservList = reservService.searchReservList(storeId, reservDate);
		log.info(reservList);
		map.put("list", reservList);
		return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
	}
	
	/**
	 * @param storeId
	 * @param reservDate
	 * @param reservHour
	 * @param reservMin
	 * @return Map<String, Object>
	 */
	@GetMapping("/choiceDay/{storeId}/{reservDate}/{reservHour}/{reservMin}")
	public ResponseEntity<Map<String, Object>> choiceDayReservInfo(@PathVariable("storeId") int storeId, @PathVariable("reservDate") String reservDate, @PathVariable("reservHour") String reservHour, @PathVariable("reservMin") String reservMin) {
		log.info("choiceDayReservInfo");
		Map<String, Object> map = new HashMap<>();
		
		ReservVO reservVO = new ReservVO();
		reservVO.setStoreId(storeId);
		reservVO.setReservDate(reservDate);
		reservVO.setReservHour(reservHour);
		reservVO.setReservMin(reservMin);
		List<JoinReservUserNameVO> list = reservService.searchReservList(reservVO);
		log.info("list : " + list);
		
		map.put("list", list);
		return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
	}
	
	/**
	 * @param cancelList
	 * @return Map<String, Object>
	 */
	@PostMapping("/cancel/{requestType}")
	public ResponseEntity<Integer> createdCancelHistory(@RequestBody List<ReservCancelVO> cancelList, @PathVariable("requestType") String requestType,
			@AuthenticationPrincipal CustomUser customUser) {
		log.info("createdCancelHistory()");
		int result = 0;
		
		if(cancelList.size() > 0) {
			result = reservService.createdCancelHistory(cancelList, requestType, customUser);
		}
		
		log.info("result : " + result);
		
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	/**
	 * @param cancelList
	 * @return ResponseEntity<Integer>
	 */
	@PutMapping("/cancel/status")
	public ResponseEntity<Integer> updateCancelStatusByReservId(@RequestBody List<ReservCancelVO> cancelList) {
		int result = reservService.updateCancelStatusByList(cancelList);
		log.info(result);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

}
