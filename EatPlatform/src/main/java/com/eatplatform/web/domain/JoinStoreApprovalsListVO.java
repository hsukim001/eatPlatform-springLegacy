package com.eatplatform.web.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JoinStoreApprovalsListVO {
	private int storeId;
	private int approvals;
	private Date storeApprovalsRegDate;
	private String storeUserId;
	private String storeName;
	private String phone;
	private String name;
}
