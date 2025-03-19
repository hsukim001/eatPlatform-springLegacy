package com.eatplatform.web.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JoinBusinessRequestWithUserAndRoleListVO {
	private int businessRequestId;
	private String requestStatus;
	private Date businessRequestRegDate;
	private Date businessReqeustUpdateDate;
	private int userId;
	private String username;
	private String password;
	private String email;
	private String phone;
	private String name;
	private int active;
	private Date regDate;
	private int roleId;
	private String roleName;
}
