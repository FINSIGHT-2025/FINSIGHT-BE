package com.finsight.backend.service;

import com.finsight.backend.dto.WatchListDTO;
import com.finsight.backend.mapper.WatchListMapper;
import com.finsight.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchListService {

    private final WatchListMapper watchListMapper;
    private final JwtUtil jwtUtil;

    public List<WatchListDTO> getWatchListDepositByUserId(String userId) {
        System.out.println("Service: 사용자 ID [" + userId + "]의 찜한상품(예금)을 조회합니다.");
        return watchListMapper.selectWatchListDepositByUserId(userId);
    }
}
/*
         d.product_name, -- 예금) 상품명
         o.d_option_save_trm, -- 예금) 저축기간
         o.d_option_intr_rate, -- 예금) 기준금리
         o.d_option_intr_rate2 -- 예금) 최고금리
 */