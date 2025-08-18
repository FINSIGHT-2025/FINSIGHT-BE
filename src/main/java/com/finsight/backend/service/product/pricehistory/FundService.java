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
        List<PricePointVO> navList = etfPriceService.getThreeMonthsPriceHistory("fund_nav", fundCode);
        List<PricePointVO> aumList = etfPriceService.getThreeMonthsPriceHistory("fund_aum", fundCode);

        // NAV와 AUM 데이터를 인덱스 기준으로 매칭하여 DTO 생성
        int size = Math.min(navList.size(), aumList.size());
        System.out.println("Fetched points count: " + size);
        System.out.println("NAV list size: " + navList.size());
        System.out.println("AUM list size: " + aumList.size());
        
        // 3개월 데이터 충분성 확인
        if (size < 90) {
            System.out.println("Warning: 3개월 데이터가 부족합니다. 최소 90일 데이터가 필요합니다.");
        }
        
        return IntStream.range(0, size)
                .mapToObj(i -> {
                    double currentNav = navList.get(i).getValue();
                    double currentAum = aumList.get(i).getValue();
                    
                    // 수익률 계산
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
    
    private double calculateWeeklyReturn(List<PricePointVO> navList, int currentIndex, int totalSize) {
        if (currentIndex < totalSize - 7) {
            return 0.0;
        }
        
        if (currentIndex == totalSize - 7) {
            return 0.0;
        }
        
        double currentNav = navList.get(currentIndex).getValue();
        double weekAgoNav = navList.get(totalSize - 7).getValue();
        
        if (currentNav == 0.0) {
            return 0.0;
        }
        
        if (weekAgoNav == 0.0) {
            double fallbackNav = findNearestNonZeroNav(navList, totalSize - 7, 0);
            if (fallbackNav == 0.0) {
                return 0.0;
            }
            weekAgoNav = fallbackNav;
        }
        
        double returnRate = ((currentNav - weekAgoNav) / weekAgoNav) * 100.0;
        return Math.round(returnRate * 100.0) / 100.0;
    }
    
    private double calculateMonthlyReturn(List<PricePointVO> navList, int currentIndex, int totalSize) {
        if (currentIndex < totalSize - 30) {
            return 0.0;
        }
        
        if (currentIndex == totalSize - 30) {
            return 0.0;
        }
        
        double currentNav = navList.get(currentIndex).getValue();
        double monthAgoNav = navList.get(totalSize - 30).getValue();
        
        if (currentNav == 0.0) {
            return 0.0;
        }
        
        if (monthAgoNav == 0.0) {
            double fallbackNav = findNearestNonZeroNav(navList, totalSize - 30, 0);
            if (fallbackNav == 0.0) {
                return 0.0;
            }
            monthAgoNav = fallbackNav;
        }
        
        double returnRate = ((currentNav - monthAgoNav) / monthAgoNav) * 100.0;
        return Math.round(returnRate * 100.0) / 100.0;
    }
    
    private double calculateQuarterlyReturn(List<PricePointVO> navList, int currentIndex, int totalSize) {
        if (totalSize < 90) {
            System.out.println("3개월 데이터 부족: " + totalSize + "일. 가장 오래된 데이터를 기준점으로 사용합니다.");
            
            if (currentIndex == 0) {
                return 0.0;
            }
            
            double currentNav = navList.get(currentIndex).getValue();
            double oldestNav = navList.get(0).getValue();
            
            if (currentNav == 0.0) {
                return 0.0;
            }
            
            if (oldestNav == 0.0) {
                double fallbackNav = findNearestNonZeroNavInDirection(navList, 0, 1);
                if (fallbackNav == 0.0) {
                    return 0.0;
                }
                oldestNav = fallbackNav;
            }
            
            double returnRate = ((currentNav - oldestNav) / oldestNav) * 100.0;
            return Math.round(returnRate * 100.0) / 100.0;
        }
        
        if (currentIndex < totalSize - 90) {
            return 0.0;
        }
        
        if (currentIndex == totalSize - 90) {
            return 0.0;
        }
        
        double currentNav = navList.get(currentIndex).getValue();
        double quarterAgoNav = navList.get(totalSize - 90).getValue();
        
        if (currentNav == 0.0) {
            return 0.0;
        }
        
        if (quarterAgoNav == 0.0) {
            System.out.println("3달 전 NAV가 0입니다. 대체 기준점을 찾습니다.");
            double fallbackNav = findNearestNonZeroNavInDirection(navList, totalSize - 90, -1);
            
            if (fallbackNav == 0.0) {
                System.out.println("대체 기준점을 찾지 못했습니다.");
                return 0.0;
            }
            
            System.out.println("대체 기준점 NAV: " + fallbackNav);
            quarterAgoNav = fallbackNav;
        }
        
        double returnRate = ((currentNav - quarterAgoNav) / quarterAgoNav) * 100.0;
        return Math.round(returnRate * 100.0) / 100.0;
    }
    
    private double findNearestNonZeroNav(List<PricePointVO> navList, int startIndex, int direction) {
        if (direction == 0) {
            double pastNav = findNearestNonZeroNavInDirection(navList, startIndex, -1);
            double futureNav = findNearestNonZeroNavInDirection(navList, startIndex, 1);
            
            if (pastNav == 0.0 && futureNav == 0.0) {
                return 0.0;
            } else if (pastNav == 0.0) {
                return futureNav;
            } else if (futureNav == 0.0) {
                return pastNav;
            } else {
                int pastDistance = startIndex - findIndexByValue(navList, pastNav, startIndex, -1);
                int futureDistance = findIndexByValue(navList, futureNav, startIndex, 1) - startIndex;
                
                return pastDistance <= futureDistance ? pastNav : futureNav;
            }
        } else {
            return findNearestNonZeroNavInDirection(navList, startIndex, direction);
        }
    }
    
    private double findNearestNonZeroNavInDirection(List<PricePointVO> navList, int startIndex, int direction) {
        int currentIndex = startIndex + direction;
        
        while (currentIndex >= 0 && currentIndex < navList.size()) {
            double nav = navList.get(currentIndex).getValue();
            if (nav != 0.0) {
                return nav;
            }
            currentIndex += direction;
        }
        
        return 0.0;
    }
    
    private int findIndexByValue(List<PricePointVO> navList, double targetNav, int startIndex, int direction) {
        int currentIndex = startIndex + direction;
        
        while (currentIndex >= 0 && currentIndex < navList.size()) {
            double nav = navList.get(currentIndex).getValue();
            if (nav == targetNav) {
                return currentIndex;
            }
            currentIndex += direction;
        }
        
        return -1;
    }
}
