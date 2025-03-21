package com.eatplatform.web.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
	private int reservLimit;
	private int seat;
	private String businessHour;
	private String storeComment;
	private String description;
	private LocalDate storeRegDate;
	private LocalDate storeUpdateDate;
	private BigDecimal score;
	
	// 이미지 첨부
	private List<StoreImageVO> storeImageList;

	public List<StoreImageVO> getStoreImageList() {
		if (storeImageList == null) {
			storeImageList = new ArrayList<StoreImageVO>();
		}
		return storeImageList;
	}
}
