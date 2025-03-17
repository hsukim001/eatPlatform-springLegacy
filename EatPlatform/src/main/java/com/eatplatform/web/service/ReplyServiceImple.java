package com.eatplatform.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.domain.ReplyVO;
import com.eatplatform.web.persistence.ReplyMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReplyServiceImple implements ReplyService{

	@Autowired
	private ReplyMapper replyMapper;
	
	@Autowired
	private NotificationService notificationService;
	
	@Transactional(value = "transactionManager")
	@Override
	public int createReply(ReplyVO replyVO) {
		int result = replyMapper.insert(replyVO);
		
		// 리뷰 등록 알림 전송
		notificationService.addReplyNotification(replyVO);
		
		return result;
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
	
	// 페이징
	@Override
	public int countAllRepliewsByReviewId(int reviewId) {
		log.info("getAllReviewsByStoreId()");
		return replyMapper.countRepliewsByReviewId(reviewId);
	}

	// username 조회
	@Override
	public ReplyVO getReplyWithUsername(int replyId) {
		log.info("getReplyWithUsername()");
		ReplyVO replyVO = replyMapper.findReplyWithUsername(replyId);
		log.info("replyVO : " + replyVO);
		return replyVO;
	}

}
