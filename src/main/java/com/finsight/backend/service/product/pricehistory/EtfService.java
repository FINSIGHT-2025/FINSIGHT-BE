package com.finsight.backend.service.product.pricehistory;

import com.finsight.backend.dto.response.EtfPriceHistoryDto;
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
public class EtfService {

    private final EtfPriceService etfPriceService;

    public List<EtfPriceHistoryDto> getThreeMonthsEtfNav(String productCode) {
        List<PricePointVO> points = etfPriceService.getThreeMonthsPriceHistory("etf_nav", productCode);
        System.out.println("Fetched points count: " + points.size());  // 디버깅용
        
        // 3개월 데이터가 충분한지 확인
        if (points.size() < 90) {
            System.out.println("Warning: 3개월 데이터가 부족합니다. 최소 90일 데이터가 필요합니다.");
        }
        
        return IntStream.range(0, points.size())
                .mapToObj(i -> {
                    double currentNav = points.get(i).getValue();
                    
                    // 수익률 계산 (소수점 둘째자리까지)
                    // weekly: 1주일 전이 0% 기준점, monthly: 1달 전이 0% 기준점
                    // quarterly: 90일 전이 0% 기준점
                    double weeklyReturn = calculateWeeklyReturn(points, i, points.size());
                    double monthlyReturn = calculateMonthlyReturn(points, i, points.size());
                    double quarterlyReturn = calculateQuarterlyReturn(points, i, points.size());
                    
                    return EtfPriceHistoryDto.builder()
                            .baseDate(points.get(i).getTime().atZone(ZoneId.systemDefault()).toLocalDate())
                            .etfNav(currentNav)
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
