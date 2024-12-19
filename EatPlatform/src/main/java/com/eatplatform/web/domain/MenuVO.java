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
public class MenuVO {
	private int menuId;
	private int storeId;
	private String menuName;
	private int menuPrice;
	private String menuComment;
	private int represent;
	private String menuImg;
	private LocalDate menuRegDate;
}
