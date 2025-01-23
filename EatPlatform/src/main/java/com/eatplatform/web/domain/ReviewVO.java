package com.eatplatform.web.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
	private String userId;
	private int reviewStar;
	private String reviewContent;
	private String reviewTag;
	private LocalDateTime reviewDate;
	private int reviewLike;
	private int reviewReport;
	
	private List<ReviewImageVO> reviewImageList;
	
	public List<ReviewImageVO> getreviewImageList() {
		if(reviewImageList == null) {
			reviewImageList = new ArrayList<ReviewImageVO>();
		}
		return reviewImageList;
	}

}
