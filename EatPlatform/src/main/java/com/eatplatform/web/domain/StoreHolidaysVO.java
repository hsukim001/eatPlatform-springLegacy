package com.eatplatform.web.domain;

import lombok.Getter;
import lombok.ToString;
import lombok.Setter;

@Getter
@Setter
@ToString
public class StoreHolidaysVO {
	private int storeHolidaysId;
	private int storeId;
	private String holiday;
}
