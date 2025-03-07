package com.eatplatform.web.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReservCancelVO {
	private int reservCancelId;
	private String requestType;
	private String cancelComment;
	private int reservId;
}
