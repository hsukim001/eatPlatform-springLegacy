package com.eatplatform.web.domain;

import java.sql.Date;

import lombok.Getter;
import lombok.ToString;
import lombok.Setter;

@Getter
@Setter
@ToString
public class JoinStoreWithStoreApprovalsVO {
	private int storeId;
	private String storeName;
	private String ownerName;
	private String storeComment;
	private Date storeUpdateDate;
	private int approvals;
}
