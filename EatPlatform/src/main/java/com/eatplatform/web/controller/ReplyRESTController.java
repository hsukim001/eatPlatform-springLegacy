package com.eatplatform.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.ReplyVO;
import com.eatplatform.web.service.ReplyService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping(value = "/reply")
@Log4j
public class ReplyRESTController {
	@Autowired
	private ReplyService replyService;
	
	// 댓글 등록(사업자)
	@PostMapping
	public ResponseEntity<Integer> createReply(
			@RequestBody ReplyVO replyVO,
			@AuthenticationPrincipal UserDetails userDetails) {
		
		String userId = userDetails.getUsername();
		replyVO.setUserId(userId);
		
		// 댓글 내용 길이 제한 (100자 이하) 
		if (replyVO.getReplyContent() != null && 
			replyVO.getReplyContent().length() > 100) {
			return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
		}
		log.info("createReply()");
		
		int result = replyService.createReply(replyVO);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	// 리뷰 댓글 전체 조회
	@GetMapping("/all/{reviewId}")
	public ResponseEntity<List<ReplyVO>> getAllReply(
			@PathVariable("reviewId") int reviewId) {
		log.info("getAllReply()");
		
		List<ReplyVO> list = replyService.getAllReply(reviewId);
		return new ResponseEntity<List<ReplyVO>>(list, HttpStatus.OK);
	}
	
	// 댓글 수정
	@PutMapping("/{replyId}")
	public ResponseEntity<Integer> updateReply(
			@PathVariable("replyId") int replyId, @RequestBody ReplyVO replyVO) {
		log.info("updateReply()");
		
		int result = replyService.updateReply(replyId, replyVO.getReplyContent());
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	// 댓글 삭제
	@DeleteMapping("/{replyId}")
	public ResponseEntity<Integer> deleteReply(
			@PathVariable("replyId") int replyId) {
		log.info("deleteReply()");
		
		int result = replyService.deleteReply(replyId);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
}
