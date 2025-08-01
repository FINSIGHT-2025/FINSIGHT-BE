package com.finsight.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewsByKeywordResponseDTO {

    private List<NewsResponseDTO> newsList;
//    private List<ProductVO> productList; // 키워드에 해당되는 상품 목록
}