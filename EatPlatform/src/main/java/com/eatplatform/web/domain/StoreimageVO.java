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
public class StoreimageVO {
	private Integer StoreImageId;
	private int StoreId;
	private String StoreImagePath;
	private String StoreImageRealName;
	private String StoreImageChgName;
	private String StoreImageExtension;
	private Date StoreImageDate;
}
