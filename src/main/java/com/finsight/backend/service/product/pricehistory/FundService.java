package com.finsight.backend.service.product.pricehistory;

import com.finsight.backend.dto.response.FundPriceHistoryDto;
import com.finsight.backend.tmptradeserverwebsocket.service.EtfPriceService;
import com.finsight.backend.domain.vo.product.PricePointVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class FundService {

    private final EtfPriceService etfPriceService;

    public List<FundPriceHistoryDto> getFundPriceHistory(String fundCode) {
        // fund_nav와 fund_aum을 각각 조회
        List<PricePointVO> navList = etfPriceService.getThreeMonthsPriceHistory("fund_nav", fundCode, "fund_code");
        List<PricePointVO> aumList = etfPriceService.getThreeMonthsPriceHistory("fund_aum", fundCode, "fund_code");

        // 두 리스트가 날짜 기준으로 동일하다고 가정하고 인덱스 기준으로 DTO 생성
        int size = Math.min(navList.size(), aumList.size());
        System.out.println("Fetched points count: " + navList.size());  // 디버깅용
        return IntStream.range(0, size)
                .mapToObj(i -> FundPriceHistoryDto.builder()
                        .baseDate(navList.get(i).getTime().atZone(ZoneId.systemDefault()).toLocalDate())
                        .fundNav(navList.get(i).getValue())
                        .fundAum(aumList.get(i).getValue())
                        .build())
                .collect(Collectors.toList());
    }
}
