package com.eatplatform.web.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HolidayVO {
	private int holidayId;
	private int storeId;
	private String holiday;
}
