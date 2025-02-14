package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.ReplyVO;

@Mapper
public interface ReplyMapper {
	List<ReplyVO> selectByReviewId(int reviewId);
	int insert(ReplyVO replyVO);
	int update(ReplyVO replyVO);
	int delete(int replyId);
	int deleteByReviewId(int reviewId);
	
	// username 조회
	ReplyVO findReplyWithUsername(int replyId);
	
}
