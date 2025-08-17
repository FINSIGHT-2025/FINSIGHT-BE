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
        // fund_nav와 fund_aum을 각각 조회 (3개월 데이터)
        List<PricePointVO> navList = etfPriceService.getThreeMonthsPriceHistory("fund_nav", fundCode);
        List<PricePointVO> aumList = etfPriceService.getThreeMonthsPriceHistory("fund_aum", fundCode);

        // 두 리스트가 날짜 기준으로 동일하다고 가정하고 인덱스 기준으로 DTO 생성
        int size = Math.min(navList.size(), aumList.size());
        System.out.println("Fetched points count: " + size);  // 디버깅용
        System.out.println("NAV list size: " + navList.size());  // NAV 리스트 크기
        System.out.println("AUM list size: " + aumList.size());  // AUM 리스트 크기
        
        // 3개월 데이터가 충분한지 확인
        if (size < 90) {
            System.out.println("Warning: 3개월 데이터가 부족합니다. 최소 90일 데이터가 필요합니다.");
        }
        
        return IntStream.range(0, size)
                .mapToObj(i -> {
                    double currentNav = navList.get(i).getValue();
                    double currentAum = aumList.get(i).getValue();
                    
                    // 수익률 계산 (소수점 둘째자리까지)
                    // weekly: 1주일 전이 0% 기준점, monthly: 1달 전이 0% 기준점
                    // quarterly: 90일 전이 0% 기준점
                    double weeklyReturn = calculateWeeklyReturn(navList, i, size);
                    double monthlyReturn = calculateMonthlyReturn(navList, i, size);
                    double quarterlyReturn = calculateQuarterlyReturn(navList, i, size);
                    
                    return FundPriceHistoryDto.builder()
                            .baseDate(navList.get(i).getTime().atZone(ZoneId.systemDefault()).toLocalDate())
                            .fundNav(currentNav)
                            .fundAum(currentAum)
                            .weeklyReturn(weeklyReturn)
                            .monthlyReturn(monthlyReturn)
                            .quarterlyReturn(quarterlyReturn)
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 1주일 전이 0% 기준점입니다.
     * 1주일 전보다 먼 과거는 0.0을 반환하고, 1주일 전은 0.0, 1주일 전 이후는 수익률을 계산합니다.
     */
    private double calculateWeeklyReturn(List<PricePointVO> navList, int currentIndex, int totalSize) {
        // 1주일 전보다 먼 과거는 0.0
        if (currentIndex < totalSize - 7) {
            return 0.0;
        }
        
        // 1주일 전이면 0.0 (기준점)
        if (currentIndex == totalSize - 7) {
            return 0.0;
        }
        
        // 1주일 전 이후부터는 1주일 전 대비 수익률 계산
        double currentNav = navList.get(currentIndex).getValue();
        double weekAgoNav = navList.get(totalSize - 7).getValue(); // 1주일 전 NAV
        
        if (weekAgoNav == 0.0) {
            return 0.0;
        }
        
        double returnRate = ((currentNav - weekAgoNav) / weekAgoNav) * 100.0;
        return Math.round(returnRate * 100.0) / 100.0;
    }
    
    /**
     * 1달 전이 0% 기준점입니다.
     * 1달 전보다 먼 과거는 0.0을 반환하고, 1달 전은 0.0, 1달 전 이후는 수익률을 계산합니다.
     */
    private double calculateMonthlyReturn(List<PricePointVO> navList, int currentIndex, int totalSize) {
        // 1달 전보다 먼 과거는 0.0
        if (currentIndex < totalSize - 30) {
            return 0.0;
        }
        
        // 1달 전이면 0.0 (기준점)
        if (currentIndex == totalSize - 30) {
            return 0.0;
        }
        
        // 1달 전 이후부터는 1달 전 대비 수익률 계산
        double currentNav = navList.get(currentIndex).getValue();
        double monthAgoNav = navList.get(totalSize - 30).getValue(); // 1달 전 NAV
        
        if (monthAgoNav == 0.0) {
            return 0.0;
        }
        
        double returnRate = ((currentNav - monthAgoNav) / monthAgoNav) * 100.0;
        return Math.round(returnRate * 100.0) / 100.0;
    }
    
    /**
     * 3달 전이 0% 기준점입니다.
     * 3달 전보다 먼 과거는 0.0을 반환하고, 3달 전은 0.0, 3달 전 이후는 수익률을 계산합니다.
     * 만약 3달 전 기준점이 0이라면 90일 전과 가장 가까운 0이 아닌 데이터를 기준으로 사용합니다.
     */
    private double calculateQuarterlyReturn(List<PricePointVO> navList, int currentIndex, int totalSize) {
        // 3개월 데이터가 부족한 경우 처리
        if (totalSize < 90) {
            System.out.println("3개월 데이터 부족: " + totalSize + "일. 3달 수익률 계산 불가.");
            return 0.0;
        }
        
        if (currentIndex < 90) {
            return 0.0; // 3달 전보다 먼 과거
        }
        
        // 3달 전이면 0.0 (기준점)
        if (currentIndex == 90) {
            return 0.0;
        }
        
        // 3달 전 이후부터는 3달 전 대비 수익률 계산
        double currentNav = navList.get(currentIndex).getValue();
        double quarterAgoNav = navList.get(90).getValue(); // 3달 전 NAV (고정된 기준점)
        
        System.out.println("3달 전 NAV (인덱스 90): " + quarterAgoNav + ", 현재 NAV: " + currentNav);
        
        // 3달 전 기준점이 0이라면 90일 전과 가장 가까운 0이 아닌 데이터를 찾아서 기준으로 사용
        if (quarterAgoNav == 0.0) {
            System.out.println("3달 전 NAV가 0입니다. 대체 기준점을 찾습니다.");
            // 90일 전부터 시작해서 0이 아닌 데이터 찾기
            double fallbackNav = findNearestNonZeroNav(navList, 90);
            
            if (fallbackNav == 0.0) {
                System.out.println("대체 기준점을 찾지 못했습니다.");
                return 0.0; // 0이 아닌 데이터를 찾지 못한 경우
            }
            
            System.out.println("대체 기준점 NAV: " + fallbackNav);
            // 찾은 기준점 대비 수익률 계산
            double returnRate = ((currentNav - fallbackNav) / fallbackNav) * 100.0;
            return Math.round(returnRate * 100.0) / 100.0;
        }
        
        double returnRate = ((currentNav - quarterAgoNav) / quarterAgoNav) * 100.0;
        return Math.round(returnRate * 100.0) / 100.0;
    }
    
    /**
     * 90일 전부터 시작해서 0이 아닌 가장 가까운 NAV를 찾습니다.
     * @param navList NAV 데이터 리스트
     * @param startIndex 시작 인덱스 (90일 전)
     * @return 0이 아닌 NAV 값, 찾지 못하면 0.0
     */
    private double findNearestNonZeroNav(List<PricePointVO> navList, int startIndex) {
        // 90일 전부터 시작해서 0이 아닌 데이터 찾기
        for (int i = startIndex; i < navList.size(); i++) {
            double nav = navList.get(i).getValue();
            if (nav != 0.0) {
                return nav;
            }
        }
        return 0.0; // 0이 아닌 데이터를 찾지 못한 경우
    }
}
