package com.finsight.backend.service.product.pricehistory;

import com.finsight.backend.dto.response.EtfPriceHistoryDto;
import com.finsight.backend.tmptradeserverwebsocket.service.EtfPriceService;
import com.finsight.backend.domain.vo.product.PriceVolumePointVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EtfService {

    private final EtfPriceService etfPriceService;

    public List<EtfPriceHistoryDto> getTwoMinutesEtfPriceVolumeHistory(String productCode) {
        List<PriceVolumePointVO> priceVolumePoints = etfPriceService.getTwoMinutesEtfPriceVolumeHistory(productCode);
        
        if (priceVolumePoints.isEmpty()) {
            System.out.println("Warning: 2분간 가격/거래량 데이터가 없습니다.");
            return List.of();
        }
        
        return priceVolumePoints.stream()
                .map(point -> EtfPriceHistoryDto.builder()
                        .timestamp(point.getTime().toEpochMilli())
                        .currentPrice(point.getPrice())
                        .currentVolume(point.getVolume())
                        .build())
                .collect(Collectors.toList());
    }
}
