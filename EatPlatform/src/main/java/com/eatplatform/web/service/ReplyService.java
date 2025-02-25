package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.ReplyVO;

public interface ReplyService {
	List<ReplyVO> getAllReply(int revewId);
	int createReply(ReplyVO replyVO);
	/**
	 * 댓글 수정
	 * @param replyId
	 * @param replyContent
	 * @return
	 */
	int updateReply(int replyId, String replyContent);
	int deleteReply(int replyId);
	int deleteReplyByReview(int reviewId);
	
	// 페이징
	int countAllRepliewsByReviewId(int reviewId);
	
	// username 조회
	ReplyVO getReplyWithUsername(int replyId);
	
}
