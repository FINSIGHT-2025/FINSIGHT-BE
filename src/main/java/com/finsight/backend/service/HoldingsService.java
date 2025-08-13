package com.finsight.backend.service;

import com.finsight.backend.dto.response.HoldingsResponseDto;
import com.finsight.backend.dto.response.HoldingDetailDto;
import com.finsight.backend.repository.mapper.HoldingsMapper;
import com.finsight.backend.repository.mapper.UserMapper;
import com.finsight.backend.common.util.JwtUtil;

import com.finsight.backend.tmptradeserverwebsocket.service.EtfPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HoldingsService {

    private final HoldingsMapper holdingsMapper;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final EtfPriceService etfPriceService;
    // 모델포트폴리오 - 나의 자산 구성 ==============================================================-->
    public Double getDomesticEquity2ByUserId(String userId) {
        Double domesticEquity2 =  holdingsMapper.selectDomesticEquity2ByUserId(userId);
        if (domesticEquity2 == null) {
            return  0.0;
        }
        return  domesticEquity2;
    }
    public Double getDomesticMixed5ByUserId(String userId) {
        Double domesticMixed5 = holdingsMapper.selectDomesticMixed5ByUserId(userId);
        if (domesticMixed5 == null) {
            return  0.0;
        }
        return  domesticMixed5;
    }
    public Double getBond5ByUserId(String userId) {
        Double domesticBond5 = holdingsMapper.selectDomesticBond5ByUserId(userId);
        if (domesticBond5 == null) {
            return  0.0;
        }
        return  domesticBond5;
    }
    public Double getBond6ByUserId(String userId) {
        Double domesticBond6 = holdingsMapper.selectDomesticBond6ByUserId(userId);
        if (domesticBond6 == null) {
            return  0.0;
        }
        return  domesticBond6;
    }
    public Double getDepositPriceByUserId(String userId) {
        System.out.println("Service: 사용자 ID [" + userId + "]의 현재 예금 보유금액을 조회합니다.");
        Double depositPrice = holdingsMapper.selectDepositPriceByUserId(userId);
        if (depositPrice == null) {
            System.out.println("사용자 예금 정보 없음");
            return 0.0; // 기본값
        }
        return depositPrice;
    }
    public Double getForeignEquity1ByUserId(String userId) {
        System.out.println("Service: 사용자 ID [" + userId + "]의 현재 예금 보유금액을 조회합니다.");
        Double foreignEquity1 = holdingsMapper.selectForeignEquity1ByUserId(userId);
        if (foreignEquity1 == null) {
            System.out.println("사용자 정보 없음");
            return 0.0; // 기본값
        }
        return foreignEquity1;
    }
    public Double getForeignEquity2ByUserId(String userId) {
        System.out.println("Service: 사용자 ID [" + userId + "]의 현재 예금 보유금액을 조회합니다.");
        Double foreignEquity2 = holdingsMapper.selectForeignEquity2ByUserId(userId);
        if (foreignEquity2 == null) {
            System.out.println("사용자 정보 없음");
            return 0.0; // 기본값
        }
        return foreignEquity2;
    }
    public Double getForeignEquity3ByUserId(String userId) {
        System.out.println("Service: 사용자 ID [" + userId + "]의 현재 예금 보유금액을 조회합니다.");
        Double foreignEquity3 = holdingsMapper.selectForeignEquity3ByUserId(userId);
        if (foreignEquity3 == null) {
            System.out.println("사용자 정보 없음");
            return 0.0; // 기본값
        }
        return foreignEquity3;
    }
    public Double getForeignBond4ByUserId(String userId) {
        System.out.println("Service: 사용자 ID [" + userId + "]의 현재 예금 보유금액을 조회합니다.");
        Double foreignBond4 = holdingsMapper.selectForeignBond4ByUserId(userId);
        if (foreignBond4 == null) {
            System.out.println("사용자 정보 없음");
            return 0.0; // 기본값
        }
        return foreignBond4;
    }

    // 보유내역
    public Double getDepositByUserId(String userId) {
        Double depositByUserId = holdingsMapper.selectDepositByUserId(userId);
        if (depositByUserId == null) {
            return  0.0;
        }
        return depositByUserId;
    }
    public Double getDomesticByUserId(String userId) {
        Double domesticByUserId = holdingsMapper.selectDomesticByUserId(userId);
        if (domesticByUserId == null) {
            return  0.0;
        }
        return domesticByUserId;
    }
    public Double getForeignByUserId(String userId) {
        Double foreignByUserId = holdingsMapper.selectForeignByUserId(userId);
        if (foreignByUserId == null) {
            return  0.0;
        }
        return foreignByUserId;
    }

    // 보유내역 전체 조회
    public HoldingsResponseDto getHoldingsByUserId(String userId) {
        // 0. 상품 타입별 보유내역 조회 (펀드, ETF, 예금)
        List<HoldingDetailDto> fundHoldings = getFundHoldingsWithPriceInfo(userId);
        List<HoldingDetailDto> etfHoldings = getEtfHoldingsWithPriceInfo(userId);
        List<HoldingDetailDto> depositHoldings = holdingsMapper.selectDepositHoldingsByUserId(userId);
        
        // 1. 요약 정보 계산 (펀드만)
        Double totalValuation = calculateFundTotalValuation(fundHoldings);
        String message = "핀사이트에 가입하고 난 후의 평가액입니다!";
        
        // 2. 투자 모아보기 계산 (펀드만)
        Double timeDepositAmount = calculateDepositAmount(depositHoldings);
        Double domesticAmount = calculateFundDomesticAmount(fundHoldings);
        Double foreignAmount = calculateFundForeignAmount(fundHoldings);
        
        return HoldingsResponseDto.builder()
                .totalValuation(totalValuation)
                .message(message)
                .timeDeposit(timeDepositAmount)
                .domesticInvestment(domesticAmount)
                .foreignInvestment(foreignAmount)
                .fundHoldings(fundHoldings)
                .etfHoldings(etfHoldings)
                .depositHoldings(depositHoldings)
                .build();
    }
    
    // 펀드 보유내역 조회 (가격 정보 포함)
    private List<HoldingDetailDto> getFundHoldingsWithPriceInfo(String userId) {
        List<HoldingDetailDto> fundHoldings = holdingsMapper.selectFundHoldingsByUserId(userId);
        
        for (HoldingDetailDto holding : fundHoldings) {
            try {
                String productCode = holding.getProductCode();
                
                // 전일 기준가 조회
                Double previousDayPrice = etfPriceService.getCurrent("fund_nav", productCode);
                holding.setPreviousDayPrice(previousDayPrice);
                
                // 전일 대비 가격 변동액 조회
                Double priceChange = etfPriceService.getChangeFromYesterday("fund_nav", productCode);
                holding.setPriceChange(priceChange);
                
                // 전일 대비 가격 변동률 조회
                Double priceChangePercent = etfPriceService.getPercentChangeFromYesterday("fund_nav", productCode);
                holding.setPriceChangePercent(priceChangePercent);
                
                // 총 평가액 변동액 계산 (가격변동액 × 보유량)
                if (priceChange != null && holding.getCurrentHoldings() != null) {
                    Double totalPriceChange = priceChange * holding.getCurrentHoldings();
                    holding.setTotalPriceChange(totalPriceChange);
                }
                
            } catch (Exception e) {
                log.warn("펀드 가격 정보 조회 실패: productCode={}, error={}", holding.getProductCode(), e.getMessage());
                // 에러 발생 시 기본값 설정
                holding.setPreviousDayPrice(0.0);
                holding.setPriceChange(0.0);
                holding.setPriceChangePercent(0.0);
                holding.setTotalPriceChange(0.0);
            }
        }
        
        return fundHoldings;
    }
    
    // ETF 보유내역 조회 (etf_nav만 조회)
    private List<HoldingDetailDto> getEtfHoldingsWithPriceInfo(String userId) {
        List<HoldingDetailDto> etfHoldings = holdingsMapper.selectEtfHoldingsByUserId(userId);
        
        for (HoldingDetailDto holding : etfHoldings) {
            try {
                String productCode = holding.getProductCode();
                
                // etf_nav로 전일 기준가 조회
                Double previousDayPrice = etfPriceService.getCurrent("etf_nav", productCode);
                holding.setPreviousDayPrice(previousDayPrice);
                
                // 가격 변동 관련 정보는 설정하지 않음 (프론트에서 처리)
                holding.setPriceChange(0.0);
                holding.setPriceChangePercent(0.0);
                holding.setTotalPriceChange(0.0);
                
            } catch (Exception e) {
                log.warn("ETF 가격 정보 조회 실패: productCode={}, error={}", holding.getProductCode(), e.getMessage());
                // 에러 발생 시 기본값 설정
                holding.setPreviousDayPrice(0.0);
                holding.setPriceChange(0.0);
                holding.setPriceChangePercent(0.0);
                holding.setTotalPriceChange(0.0);
            }
        }
        
        return etfHoldings;
    }
    


    
    // 펀드 총 평가액 계산
    private Double calculateFundTotalValuation(List<HoldingDetailDto> fundHoldings) {
        double total = 0.0;
        
        for (HoldingDetailDto holding : fundHoldings) {
            if (holding.getCurrentValuation() != null) {
                total += holding.getCurrentValuation();
            }
        }
        
        return total;
    }
    
    // 예금 보유금액 계산
    private Double calculateDepositAmount(List<HoldingDetailDto> depositHoldings) {
        double total = 0.0;
        for (HoldingDetailDto holding : depositHoldings) {
            if (holding.getCurrentValuation() != null) {
                total += holding.getCurrentValuation();
            }
        }
        return total;
    }
    
    // 펀드 중 국내 투자상품 보유금액 계산
    private Double calculateFundDomesticAmount(List<HoldingDetailDto> fundHoldings) {
        double total = 0.0;
        
        for (HoldingDetailDto holding : fundHoldings) {
            if (holding.getCurrentValuation() != null && 
                holding.getProductCountry() != null && 
                holding.getProductCountry().equals("국내")) {
                total += holding.getCurrentValuation();
            }
        }
        
        return total;
    }
    
    // 펀드 중 해외 투자상품 보유금액 계산
    private Double calculateFundForeignAmount(List<HoldingDetailDto> fundHoldings) {
        double total = 0.0;
        
        for (HoldingDetailDto holding : fundHoldings) {
            if (holding.getCurrentValuation() != null && 
                holding.getProductCountry() != null && 
                holding.getProductCountry().equals("해외")) {
                total += holding.getCurrentValuation();
            }
        }
        
        return total;
    }

}