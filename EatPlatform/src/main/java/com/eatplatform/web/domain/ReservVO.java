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
public class ReservVO {
	private int reservId;
	private int storeId;
	private int userId;
	private String reservDate;
	private String reservHour;
	private String reservMin;
	private int reservPersonnel;
	private Date reservDateCreated;
	private String reservStatus;
}
