package com.finsight.backend.repository.mapper;

import com.finsight.backend.dto.response.ProductScore;
import java.util.List;

public interface KeywordProductMapper {
    // ✅ Map 대신 DTO 반환
    List<ProductScore> findProductScoresByKeyword(String keyword);
}
