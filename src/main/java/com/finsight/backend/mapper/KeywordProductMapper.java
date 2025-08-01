package com.finsight.backend.mapper;

import java.util.List;

// 키워드 → 상품 ID 리스트 조회
public interface KeywordProductMapper {
    List<String> findProductIdsByKeyword(String keyword);
}
