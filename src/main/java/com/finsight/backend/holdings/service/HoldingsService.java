package com.finsight.backend.holdings.service;

import com.finsight.backend.holdings.dto.HoldingsResponseDto;
import com.finsight.backend.holdings.mapper.HoldingsMapper;
import com.finsight.backend.holdings.vo.HistoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HoldingsService {

    private final HoldingsMapper holdingsMapper;

    public HoldingsResponseDto getHoldingsWithHistory(String userId, String productCode) {
        HoldingsResponseDto holdings = holdingsMapper.selectHoldingsByUserIdAndProductCode(userId, productCode);
        if (holdings == null) return null;

        Long holdingsId = holdings.getHoldingsId();
        List<HistoryVO> histories = holdingsMapper.selectHistoriesByHoldingsId(holdingsId);
        holdings.setHistory(histories);

        boolean isWatched = holdingsMapper.isProductWatched(userId, productCode);
        holdings.setWatched(isWatched);

        return holdings;
    }
}
