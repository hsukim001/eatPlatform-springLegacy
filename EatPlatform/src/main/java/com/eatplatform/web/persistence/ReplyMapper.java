package com.eatplatform.web.persistence;

import java.util.List;

import com.eatplatform.web.domain.ReplyVO;

public interface ReplyMapper {
	int insert(ReplyVO replyVO);
	List<ReplyVO> selectByReviewId(int reviewId);
	int update(ReplyVO replyVO);
	int delete(int replyId);
}
