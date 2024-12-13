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
public class ReservVO {
	private int reservId;
	private int storeId;
	private String userId;
	private String reservDate;
	private String reservTime;
	private LocalDate reservDateCreated;
}
