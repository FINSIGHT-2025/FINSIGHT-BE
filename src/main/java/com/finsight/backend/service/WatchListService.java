package com.finsight.backend.service;

import com.finsight.backend.dto.WatchListDTO;

public interface WatchListService {
    int insertWatch(WatchListDTO watchListDTO,  String accessToken);

    // 찜한 상품 등록
    int insertWatch(WatchListDTO watchListDTO);

    int deleteWatch(String userId, String productCode, String productCategory);
}
