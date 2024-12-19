package com.eatplatform.web.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StoreScheduleVO {
	private int storeId;
	private String storeStartTime;
	private String storeEndTime;
	private int reservLimit;
	private String time;
	private boolean active;
	
	public StoreScheduleVO(int storeId, String storeStartTime, String storeEndTime, int reservLimit) {
		this.storeId = storeId;
		this.storeStartTime = storeStartTime;
		this.storeEndTime = storeEndTime;
		this.reservLimit = reservLimit;
	}
	
	public StoreScheduleVO(String time, boolean active) {
		this.time = time;
		this.active = active;
	}
}
