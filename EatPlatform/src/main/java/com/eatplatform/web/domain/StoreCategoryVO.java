package com.eatplatform.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCategoryVO {
    private Integer storeId;
    private Integer mainCategoryId;
	private Integer subCategoryId;
	private String mainCategoryName;
	private String subCategoryName;
}
