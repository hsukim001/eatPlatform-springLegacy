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
public class UserVO {
	private String userId;
	private String userPw;
	private String userEmail;
	private String userPhone;
	private String userName;
	private String userAuth;
	private char userActiveYn;
	private Date userRegDate;
}
