package com.finsight.backend.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsProductVO {
    private Long newsProductId; // news_product_id
    private String productCode; // product_code
    private String newsId;      // news_id
}
