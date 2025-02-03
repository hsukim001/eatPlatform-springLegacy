package com.eatplatform.web.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.ReplyVO;
import com.eatplatform.web.persistence.ReplyMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReplyServiceImple implements ReplyService{

	@Autowired
	private ReplyMapper replyMapper;
	
	@Override
	public int createReply(ReplyVO replyVO) {
		log.info("createReply()");
		int insertResult = replyMapper.insert(replyVO);
		log.info(insertResult + "행 댓글 추가");
		return 1;
	}

	@Override
	public List<ReplyVO> getAllReply(int revewId) {
		log.info("getAllReply()");
		List<ReplyVO> list = replyMapper.selectByReviewId(revewId);
		return list;
	}

	@Override
	public int updateReply(int replyId, String replyContent) {
		log.info("updateReply()");
		ReplyVO replyVO = new ReplyVO();
		replyVO.setReplyId(replyId);
		replyVO.setReplyContent(replyContent);
		replyVO.setReplyUpdateDate(LocalDateTime.now());
		return replyMapper.update(replyVO);
	}

	@Override
	public int deleteReply(int replyId) {
		log.info("deleteReply()");
		int deleteResult = replyMapper.delete(replyId);
		log.info(deleteResult + "행 댓글 삭제");
		return 1;
	}

	// 해당 리뷰 댓글 삭제
	@Override
	public int deleteReplyByReview(int reviewId) {
		log.info("deleteReplyByReview()");
		return replyMapper.deleteByReviewId(reviewId);
	}

}
