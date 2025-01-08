package com.eatplatform.web.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class StoreAddressVO {
	private int storeId;
	private String sido;
	private String sigungu;
	private String bname1;
	private String bname2;
	private String roadAddress;
	private String jibunAddress;
	private String detailAddress;
	private String extraAddress;
	private int postCode;
}
