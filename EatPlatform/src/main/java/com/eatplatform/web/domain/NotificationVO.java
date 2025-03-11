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
public class NotificationVO {
	private int notificationId;
	private String type;
	private String username;
	private String message;
	private String read;
	private Date notificationDate;
	private String url;
	
}