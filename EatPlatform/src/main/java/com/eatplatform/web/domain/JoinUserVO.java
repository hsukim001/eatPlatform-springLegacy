package com.eatplatform.web.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JoinUserVO {
	private int userId;
	private String username;
	private String password;
	private String email;
	private String phone;
	private String name;
	private int active;
	private Date regDate;
	private String roleName;
}
