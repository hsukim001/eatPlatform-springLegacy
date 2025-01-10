package com.eatplatform.web.domain;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReviewReportListVO {
	
	private int reviewReportListId;
	private int reviewId;
	private String userId;
	private String reviewReportMessage;
	private String reviewContent;
	private Date reviewReportDate;
	
}
