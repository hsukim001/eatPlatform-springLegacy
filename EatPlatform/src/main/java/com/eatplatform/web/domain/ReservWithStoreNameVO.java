package com.eatplatform.web.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReservWithStoreNameVO extends ReservVO{
	private String storeName;
}
