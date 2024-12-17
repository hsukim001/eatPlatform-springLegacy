package com.eatplatform.web.util;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class DataResponse {
	private List<?> list;
	private PageMaker pageMaker;
}
