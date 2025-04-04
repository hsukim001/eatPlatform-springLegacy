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
	private String requestStatus;
	private Date businessRequestRegDate;
	private Date businessReqeustUpdateDate;
	private int userId;
}
