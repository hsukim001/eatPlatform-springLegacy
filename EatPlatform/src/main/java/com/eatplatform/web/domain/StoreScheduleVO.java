package com.eatplatform.web.domain;

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
	private String storeStartTime;
	private String storeEndTime;
	private int reservLimit;
	private String time;
	private int totalPersonnel;
	private boolean active;
	
	public StoreScheduleVO(int storeId, String storeStartTime, String storeEndTime, int reservLimit) {
		this.storeId = storeId;
		this.storeStartTime = storeStartTime;
		this.storeEndTime = storeEndTime;
		this.reservLimit = reservLimit;
	}
	
	public StoreScheduleVO(int storeId, String reservDate, int reservLimi) {
		this.storeId = storeId;
		this.reservDate = reservDate;
		this.reservLimit = reservLimi;
	}
	
	public StoreScheduleVO(String time, int totalPersonnel) {
		this.time = time;
		this.totalPersonnel = totalPersonnel;
	}
}
