package com.eatplatform.web.domain;

import java.time.LocalDateTime;
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
	private String userId;
	private int reviewId;
	private String replyContent;
	private Date replyDate;
	private LocalDateTime replyUpdateDate;
}
