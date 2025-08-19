package com.finsight.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
@Builder
public class PortfolioDto {
    private double totalAssets;
    private Map<String, AssetDetail> assets;

    @Getter
    @Builder
    public static class AssetDetail {
        private Double amount; // <-- 이렇게 수정! (소문자 d -> 대문자 D)
        private double percentage;
    }
}