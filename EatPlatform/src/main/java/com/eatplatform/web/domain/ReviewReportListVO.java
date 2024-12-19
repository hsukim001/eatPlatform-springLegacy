package com.eatplatform.web.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReviewReportListVO {
	
	private int reviewId;
	private String userId;
	private String reportMessage;
	
}
