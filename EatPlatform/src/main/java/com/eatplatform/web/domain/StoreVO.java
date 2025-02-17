package com.eatplatform.web.domain;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class StoreVO {
	private int storeId;
	private String storeUserId;
	private String storeName;
	private String storePhone;
	private String ownerName;
	private String foodCategory;
	private int reservLimit;
	private int seat;
	private String businessHour;
	private String storeComment;
	private String description;
	private LocalDate storeRegDate;
	private LocalDate storeUpdateDate;
}
