package com.eatplatform.web.domain;

import java.util.Date;

import lombok.Getter;
import lombok.ToString;
import lombok.Setter;

@Getter
@Setter
@ToString
public class RequestInfoVO {
	
	private int businessRequestId;
	private int storeId;
	private String storeName;
	private String ownerName;
	private String storePhone;
	private String foodCategory;
	private String businessHour;
	private String storeComment;
	private String description;
	private String roadAddress;
	private String jibunAddress;
	private Date regDate;
	
}
