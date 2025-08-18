package com.finsight.backend.service;

import com.finsight.backend.dto.response.HoldingsResponseDto;
import com.finsight.backend.dto.response.HoldingDetailDto;
import com.finsight.backend.dto.response.PortfolioDto;
import com.finsight.backend.repository.mapper.HoldingsMapper;
import com.finsight.backend.repository.mapper.UserMapper;
import com.finsight.backend.common.util.JwtUtil;

import com.finsight.backend.tmptradeserverwebsocket.service.EtfPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Double domesticEquity2 = holdingsMapper.selectDomesticEquity2ByUserId(userId);
        if (domesticEquity2 == null) {
            return 0.0;
        }
        return domesticEquity2;
    }

    public Double getDomesticMixed5ByUserId(String userId) {
        Double domesticMixed5 = holdingsMapper.selectDomesticMixed5ByUserId(userId);
        if (domesticMixed5 == null) {
            return 0.0;
        }
        return domesticMixed5;
    }

    public Double getBond5ByUserId(String userId) {
        Double domesticBond5 = holdingsMapper.selectDomesticBond5ByUserId(userId);
        if (domesticBond5 == null) {
            return 0.0;
        }
        return domesticBond5;
    }

    public Double getBond6ByUserId(String userId) {
        Double domesticBond6 = holdingsMapper.selectDomesticBond6ByUserId(userId);
        if (domesticBond6 == null) {
            return 0.0;
        }
        return domesticBond6;
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
            return 0.0;
        }
        return depositByUserId;
    }

    public Double getDomesticByUserId(String userId) {
        Double domesticByUserId = holdingsMapper.selectDomesticByUserId(userId);
        if (domesticByUserId == null) {
            return 0.0;
        }
        return domesticByUserId;
    }

    public Double getForeignByUserId(String userId) {
        Double foreignByUserId = holdingsMapper.selectForeignByUserId(userId);
        if (foreignByUserId == null) {
            return 0.0;
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

        return Math.round(total * 100.0) / 100.0;
    }

    // 예금 보유금액 계산
    private Double calculateDepositAmount(List<HoldingDetailDto> depositHoldings) {
        double total = 0.0;
        for (HoldingDetailDto holding : depositHoldings) {
            if (holding.getCurrentValuation() != null) {
                total += holding.getCurrentValuation();
            }
        }
        return Math.round(total * 100.0) / 100.0;
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

        return Math.round(total * 100.0) / 100.0;
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

        return Math.round(total * 100.0) / 100.0;
    }

    // 예금 상품별 개별 보유내역 조회 (holdingsID당 가장 최근 history)
    public List<HoldingDetailDto> getDepositHoldingsByUserId(String userId) {
        return holdingsMapper.selectDepositHoldingsByUserId(userId);
    }

    // 예금 상품별 개별 보유내역 조회 (컨트롤러용)
    public List<HoldingDetailDto> getDepositDetailsByUserId(String userId) {
        return getDepositHoldingsByUserId(userId);
    }

    public PortfolioDto getPortfolioDetails(String userId) {

            // --- 1. 모든 Mapper 호출을 Optional로 안전하게 감싸줍니다. ---

            double domesticEquity2 = Optional.ofNullable(holdingsMapper.selectDomesticEquity2ByUserId(userId)).orElse(0.0);
            double domesticMixed5 = Optional.ofNullable(holdingsMapper.selectDomesticMixed5ByUserId(userId)).orElse(0.0);
            double domesticBond5 = Optional.ofNullable(holdingsMapper.selectDomesticBond5ByUserId(userId)).orElse(0.0);
            double domesticBond6 = Optional.ofNullable(holdingsMapper.selectDomesticBond6ByUserId(userId)).orElse(0.0);
            double foreignEquity1 = Optional.ofNullable(holdingsMapper.selectForeignEquity1ByUserId(userId)).orElse(0.0);
            double foreignEquity2 = Optional.ofNullable(holdingsMapper.selectForeignEquity2ByUserId(userId)).orElse(0.0);
            double foreignEquity3 = Optional.ofNullable(holdingsMapper.selectForeignEquity3ByUserId(userId)).orElse(0.0);
            double foreignBond4 = Optional.ofNullable(holdingsMapper.selectForeignBond4ByUserId(userId)).orElse(0.0);

            // [수정됨] Mapper 직접 호출 대신, 아래에 있는 헬퍼 메소드를 사용합니다.
            double depositAmount = getDepositTotalAmountByUserId(userId);

            double totalAssets = Optional.ofNullable(holdingsMapper.selectTotalHoldingsAmountByUserId(userId)).orElse(0.0);


            // --- 2. '예금'의 비중만 서비스에서 직접 계산합니다. ---
            double depositPercentage = 0.0;
            if (totalAssets > 0) {
                depositPercentage = (depositAmount / totalAssets) * 100;
            }


            // --- 3. 취합한 모든 데이터를 최종 DTO에 담습니다. ---
            Map<String, PortfolioDto.AssetDetail> assetsMap = new HashMap<>();

            assetsMap.put("domesticEquity2", PortfolioDto.AssetDetail.builder().amount(null).percentage(domesticEquity2).build());
            assetsMap.put("domesticMixed5", PortfolioDto.AssetDetail.builder().amount(null).percentage(domesticMixed5).build());
            assetsMap.put("domesticBond5", PortfolioDto.AssetDetail.builder().amount(null).percentage(domesticBond5).build());
            assetsMap.put("domesticBond6", PortfolioDto.AssetDetail.builder().amount(null).percentage(domesticBond6).build());
            assetsMap.put("foreignEquity1", PortfolioDto.AssetDetail.builder().amount(null).percentage(foreignEquity1).build());
            assetsMap.put("foreignEquity2", PortfolioDto.AssetDetail.builder().amount(null).percentage(foreignEquity2).build());
            assetsMap.put("foreignEquity3", PortfolioDto.AssetDetail.builder().amount(null).percentage(foreignEquity3).build());
            assetsMap.put("foreignBond4", PortfolioDto.AssetDetail.builder().amount(null).percentage(foreignBond4).build());
            assetsMap.put("deposit", PortfolioDto.AssetDetail.builder().amount(depositAmount).percentage(depositPercentage).build());

            // 4. 최종 결과를 반환합니다.
            return PortfolioDto.builder()
                    .totalAssets(totalAssets)
                    .assets(assetsMap)
                    .build();
    }

    // 이 메소드는 이제 위에서 올바르게 사용됩니다. (수정 없음)
    private Double getDepositTotalAmountByUserId(String userId) {
        List<HoldingDetailDto> depositDetails = getDepositDetailsByUserId(userId);
        if (depositDetails == null || depositDetails.isEmpty()) {
            return 0.0;
        }
        return depositDetails.stream()
                .mapToDouble(dto -> Optional.ofNullable(dto.getCurrentValuation()).orElse(0.0))
                .sum();
    }
}