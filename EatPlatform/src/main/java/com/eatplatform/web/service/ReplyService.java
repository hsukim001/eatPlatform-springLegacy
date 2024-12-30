package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.ReplyVO;

public interface ReplyService {
	int createReply(ReplyVO replyVO);
	List<ReplyVO> getAllReply(int revewId);
	int updateReply(int replyId, String replyContent);
	int deleteReply(int replyId);
	int deleteReplyByReview(int reviewId);
	
}
