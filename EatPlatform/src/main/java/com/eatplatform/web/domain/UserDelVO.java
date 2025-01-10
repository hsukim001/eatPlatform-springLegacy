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
public class UserDelVO {
	private String userId;
	private String userPw;
	private String userEmail;
	private String userPhone;
	private String userName;
	private String userAuth;
	private int userActive;
	private Date userRegDate;
	private Date userDelRegDate;
}
