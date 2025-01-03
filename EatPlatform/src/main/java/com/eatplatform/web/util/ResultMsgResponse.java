package com.eatplatform.web.util;

import lombok.ToString;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResultMsgResponse {
	private int result;
	private String msg;
}
