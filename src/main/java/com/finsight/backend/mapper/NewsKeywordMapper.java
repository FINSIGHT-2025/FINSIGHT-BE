package com.finsight.backend.mapper;

import java.util.List;

// 뉴스 ID → 키워드 조회
public interface NewsKeywordMapper {
    List<String> findKeywordsByNewsId(String newsId);
}
