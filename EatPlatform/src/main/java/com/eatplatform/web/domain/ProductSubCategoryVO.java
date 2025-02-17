package com.eatplatform.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSubCategoryVO {
	private Integer subCategoryId;
    private Integer mainCategoryId;
    private String subCategoryName;
}
