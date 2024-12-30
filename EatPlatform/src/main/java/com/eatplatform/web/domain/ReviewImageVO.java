package com.eatplatform.web.domain;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReviewImageVO {
	private int reviewImageId;
	private int reviewId;
	private String reviewImagePath;
	private String reviewImageRealName;
	private String reviewImageChgName;
	private String reviewImageExtension;
	private MultipartFile file;
}
