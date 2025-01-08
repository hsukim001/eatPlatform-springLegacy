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
	private String reservDate;
	private int reservLimit;
	private String hour;
	private String min;
	private int totalPersonnel;
	private boolean active;
	
	public StoreScheduleVO(String reservDate, String hour, String min, int totalPersonnel) {
		this.reservDate = reservDate;
		this.hour = hour;
		this.min = min;
		this.totalPersonnel = totalPersonnel;
	}
}
