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
public class StoreScheduleVO {
	private int storeId;
	private Date reservDate;
	private int reservLimit;
	private String time;
	private String min;
	private int totalPersonnel;
	private boolean active;
	
	public StoreScheduleVO(Date reservDate, String time, String min, int totalPersonnel) {
		this.reservDate = reservDate;
		this.time = time;
		this.min = min;
		this.totalPersonnel = totalPersonnel;
	}
}
