package com.finsight.backend.service.product.pricehistory;

import com.finsight.backend.dto.response.EtfPriceHistoryDto;
import com.finsight.backend.tmptradeserverwebsocket.service.EtfPriceService;
import com.finsight.backend.domain.vo.product.PricePointVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EtfService {

    private final EtfPriceService etfPriceService;

    public List<EtfPriceHistoryDto> getThreeMonthsEtfNav(String productCode) {
        List<PricePointVO> points = etfPriceService.getThreeMonthsPriceHistory("etf_nav", productCode, "etf_code");
        System.out.println("Fetched points count: " + points.size());  // 디버깅용
        return points.stream()
                .map(p -> EtfPriceHistoryDto.builder()
                        .baseDate(p.getTime().atZone(ZoneId.systemDefault()).toLocalDate())
                        .etfNav(p.getValue())
                        .build())
                .collect(Collectors.toList());
    }
}
