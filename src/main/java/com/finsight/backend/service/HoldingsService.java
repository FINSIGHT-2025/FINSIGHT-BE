package com.finsight.backend.service;

import com.finsight.backend.repository.mapper.HoldingsMapper;
import com.finsight.backend.repository.mapper.UserMapper;
import com.finsight.backend.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HoldingsService {
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    @Autowired
    private final HoldingsMapper holdingsMapper;
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
}