package com.finsight.backend.tmpdetailhodings.service;

import com.finsight.backend.tmpdetailhodings.dto.DetailHoldingsResponseDto;
import com.finsight.backend.repository.mapper.DetailHoldingsMapper;
import com.finsight.backend.tmpdetailhodings.vo.DetailHistoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class DetailHoldingsService {

    private final DetailHoldingsMapper detailHoldingsMapper;

    public DetailHoldingsResponseDto getHoldingsWithHistory(String userId, String productCode) {
        DetailHoldingsResponseDto holdings = detailHoldingsMapper.selectHoldingsByUserIdAndProductCode(userId, productCode);
        if (holdings == null) return null;

        Long holdingsId = holdings.getHoldingsId();
        
        // buy 타입이면서 가장 최근인 history만 가져오기
        List<DetailHistoryVO> histories = detailHoldingsMapper.selectLatestBuyHistoryByHoldingsId(holdingsId);
        
        holdings.setHistory(histories);

        return holdings;
    }
}
