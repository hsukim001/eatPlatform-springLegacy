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
public class ReviewVO {
	
	private int reviewId;
	private int storeId;
	private String userId;
	private int reviewStar;
	private String reviewContent;
	private String reviewTag;
	private Date reviewDate;
	private int reviewLike;
	private int reviewReport;

}