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
	private String storeName;
	private String userId;
	private String reservDate;
	private String reservTime;
	private int reservPersonnel;
	private Date reservDateCreated;
}