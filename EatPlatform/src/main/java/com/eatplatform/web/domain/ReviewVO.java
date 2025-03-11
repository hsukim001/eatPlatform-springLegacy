package com.eatplatform.web.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	private int userId;
	private int reviewStar;
	private String reviewContent;
	private String reviewTag;
	private Date reviewDate;
	private int reviewLike;
	private int reviewReport;
	private Date reviewUpdateDate;
	
	// 이미지 첨부
	private List<ReviewImageVO> reviewImageList;
	
	public List<ReviewImageVO> getreviewImageList() {
		if(reviewImageList == null) {
			reviewImageList = new ArrayList<ReviewImageVO>();
		}
		return reviewImageList;
	}

	// username 조회
	private String username;
	
}
