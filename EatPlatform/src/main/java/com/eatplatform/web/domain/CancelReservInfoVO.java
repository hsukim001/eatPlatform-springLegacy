package com.eatplatform.web.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CancelReservInfoVO extends ReservVO{
	private int cancelId;
	private String cancelComment;
	private String requestStatus;
	private String name;
	private String phone;
	private int reservId;
}
