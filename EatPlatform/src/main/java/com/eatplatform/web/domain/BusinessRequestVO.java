package com.eatplatform.web.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BusinessRequestVO {
	private int businessRequestId;
	private int storeId;
	private String username;
	private int requestApprovals;
	private Date businessRequestRegDate;
}
