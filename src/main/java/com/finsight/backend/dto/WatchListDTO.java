package com.finsight.backend.dto;

import com.finsight.backend.domain.enumerate.WatchListId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchListDTO {
    private WatchListId watchListId;


    // 찜한 상품 (예금)
    private String productName;       // 상품명
    private String dOptionSaveTrm;    // 저축기간
    private String dOptionIntrRate;   // 기준금리
    private String dOptionIntrRate2;  // 최고금리
}
