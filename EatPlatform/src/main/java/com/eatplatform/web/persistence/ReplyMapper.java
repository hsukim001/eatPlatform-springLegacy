package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.ReplyVO;

@Mapper
public interface ReplyMapper {
	int insert(ReplyVO replyVO);
	List<ReplyVO> selectByReviewId(int reviewId);
	int update(ReplyVO replyVO);
	int delete(int replyId);
	int deleteByReviewId(int reviewId);
	
	// 페이징
	int countRepliewsByReviewId(int reviewId);
	
	// username 조회
	ReplyVO findReplyWithUsername(int replyId);
	
}
