package com.eatplatform.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVO {

    private Integer productId;
    private Integer productStoreId;
    private String sellerId;
    private String productName;
    private Integer productPrice = 0;
    private Integer productBundle = 1;
    private Integer productStock = 0;
    private Timestamp createAt;
    private Timestamp updatedAt;
}
