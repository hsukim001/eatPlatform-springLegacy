package com.eatplatform.web.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import lombok.Getter;
import lombok.ToString;
import lombok.Setter;

@Getter
@Setter
@ToString
public class JoinStoreApprovalsInfoVO {
	private int storeId;
	private int approvals;
	private Date storeApprovalsRegDate;
	private String storeUserId;
	private String storeName;
	private String storePhone;
	private String ownerName;
	private int reservLimit;
	private int seat;
	private String businessHour;
	private String storeComment;
	private String description;
	private String roadAddress;
	private String jibunAddress;
	private String detailAddress;
	private String phone;
	private String email;
}
