package com.eatplatform.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.CustomUser;
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
	public ResponseEntity<Integer> createReply(Model model, @RequestBody ReplyVO replyVO,
			@AuthenticationPrincipal CustomUser customUser) {

		int userId = customUser.getUser().getUserId();
		replyVO.setUserId(userId);
		
		// 필수 필드 검증(공백 및 글자 수 제한)
		if (replyVO.getReplyContent() == null || replyVO.getReplyContent().trim().isEmpty()) {
			model.addAttribute("error", "Review content cannot be empty.");
			return new ResponseEntity<>(0, HttpStatus.FORBIDDEN);
		} else if (replyVO.getReplyContent() != null && replyVO.getReplyContent().length() > 200) {
			return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
		}
		log.info("createReply()");

		int result = replyService.createReply(replyVO);
		
		return new ResponseEntity<Integer>(result, HttpStatus.OK);

	}

	// 리뷰 댓글 전체 조회
	@GetMapping("/all/{reviewId}")
	public ResponseEntity<List<ReplyVO>> getAllReply(@PathVariable("reviewId") int reviewId) {
		log.info("getAllReply()");

		List<ReplyVO> list = replyService.getAllReply(reviewId);

		for (ReplyVO replyVO : list) {
			ReplyVO username = replyService.getReplyWithUsername(replyVO.getReplyId());
			replyVO.setUsername(username.getUsername());
		}
		log.info("list : " + list);
		return new ResponseEntity<List<ReplyVO>>(list, HttpStatus.OK);
	}

	// 댓글 수정
	@PutMapping("/{replyId}")
	public ResponseEntity<Integer> updateReply(Model model, @PathVariable("replyId") int replyId,
			@RequestBody ReplyVO replyVO) {
		log.info("updateReply()");

		// 필수 필드 검증(공백 및 글자 수 제한)
		if (replyVO.getReplyContent() == null || replyVO.getReplyContent().trim().isEmpty()) {
			model.addAttribute("error", "Review content cannot be empty.");
			return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
		} else if (replyVO.getReplyContent() != null && replyVO.getReplyContent().length() > 50) {
			return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
		}

		int result = replyService.updateReply(replyId, replyVO.getReplyContent());
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// 댓글 삭제
	@DeleteMapping("/{replyId}")
	public ResponseEntity<Integer> deleteReply(@PathVariable("replyId") int replyId) {
		log.info("deleteReply()");

		int result = replyService.deleteReply(replyId);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

}
