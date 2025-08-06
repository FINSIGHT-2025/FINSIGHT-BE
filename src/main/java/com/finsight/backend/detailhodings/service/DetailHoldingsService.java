package com.finsight.backend.detailhodings.service;

import com.finsight.backend.detailhodings.dto.DetailHoldingsResponseDto;
import com.finsight.backend.detailhodings.mapper.DetailHoldingsMapper;
import com.finsight.backend.detailhodings.vo.DetailHistoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetailHoldingsService {

    private final DetailHoldingsMapper detailHoldingsMapper;

    public DetailHoldingsResponseDto getHoldingsWithHistory(String userId, String productCode) {
        DetailHoldingsResponseDto holdings = detailHoldingsMapper.selectHoldingsByUserIdAndProductCode(userId, productCode);
        if (holdings == null) return null;

        Long holdingsId = holdings.getHoldingsId();
        List<DetailHistoryVO> histories = detailHoldingsMapper.selectHistoriesByHoldingsId(holdingsId);
        holdings.setHistory(histories);

        boolean isWatched = detailHoldingsMapper.isProductWatched(userId, productCode);
        holdings.setWatched(isWatched);

        return holdings;
    }
}
