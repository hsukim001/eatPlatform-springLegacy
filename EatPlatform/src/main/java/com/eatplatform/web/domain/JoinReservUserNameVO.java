package com.eatplatform.web.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JoinReservUserNameVO {
	private int reservId;
	private int storeId;
	private int userId;
	private String storeName;
	private String reservDate;
	private String reservHour;
	private String reservMin;
	private int reservPersonnel;
	private Date reservDateCreated;
	private String name;
	private String phone;
}
