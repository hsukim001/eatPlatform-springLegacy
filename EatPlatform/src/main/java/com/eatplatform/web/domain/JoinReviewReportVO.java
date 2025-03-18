package com.eatplatform.web.domain;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JoinReviewReportVO {
	private int reviewId;
	private int storeId;
	private int userId;
	private int reviewReport;
	private String reviewContent;
	private Date reviewUpdateDate;
	private String storeName;
	private String name;
}
