package com.eatplatform.web.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StoreApprovalsVO {
	private int storeId;
	private int approvals;
	private Date storeApprovalsRegDate;
}
