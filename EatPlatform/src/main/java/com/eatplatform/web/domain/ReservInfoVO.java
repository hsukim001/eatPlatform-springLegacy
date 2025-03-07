package com.eatplatform.web.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReservInfoVO extends ReservVO{
	private String storeName;
	private String storePhone;
	private String roadAddress;
	private String jibunAddress;
	private String detailAddress;
}
