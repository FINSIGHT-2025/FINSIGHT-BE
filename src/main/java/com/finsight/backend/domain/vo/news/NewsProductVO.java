package com.finsight.backend.domain.vo.news;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsProductVO {
    private String productCode; // product_code
    private String newsId;      // news_id
    private String newsProductCategory; // news_product_category
}
