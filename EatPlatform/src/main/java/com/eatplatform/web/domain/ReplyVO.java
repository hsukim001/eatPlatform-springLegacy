package com.eatplatform.web.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReplyVO {
	private int replyId;
	private int userId;
	private int reviewId;
	private String replyContent;
	private Date replyDate;
	private Date replyUpdateDate;
	
	// username 조회
	private String username;
	
}
