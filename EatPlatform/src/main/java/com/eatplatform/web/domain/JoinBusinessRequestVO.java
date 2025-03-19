package com.eatplatform.web.domain;

import java.util.Date;

import lombok.Getter;
import lombok.ToString;
import lombok.Setter;

@Getter
@Setter
@ToString
public class JoinBusinessRequestVO {
	
	private int businessRequestId;
	private String requestStatus;
	private int userId;
	private Date businessReqeustRegDate;
	private Date businessReqeustUpdateDate;
	private String username;
	private String name;
	private String phone;
	private String email;
	
}
