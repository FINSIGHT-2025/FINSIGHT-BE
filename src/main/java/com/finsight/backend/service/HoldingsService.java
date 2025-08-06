package com.finsight.backend.service;

import com.finsight.backend.mapper.HoldingsMapper;
import com.finsight.backend.mapper.UserMapper;
import com.finsight.backend.util.JwtUtil;
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

    public String getDepositPriceByUserId(String userId) {
        System.out.println("Service: 사용자 ID [" + userId + "]의 현재 예금 보유금액을 조회합니다.");
        String depositPrice = holdingsMapper.selectDepositPriceByUserId(userId);

        if (depositPrice != null) {
            System.out.println("Service: 예금 보유금액 조회 성공 - " + depositPrice);
        } else {
            System.out.println("Service: 사용자[" + userId + "]의 예금 보유금액 정보를 찾을 수 없습니다.");
        }
        return depositPrice;
    }
}
