package com.eatplatform.web.domain;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EmailVO {
	private int status;
	private String userEmail;
	private String authCode;
	private Date sendTime;
	private Date expirationTime;
	private String message;
	
}
