package com.finsight.backend.service;

import com.finsight.backend.domain.vo.product.ProductVO;
import com.finsight.backend.dto.WatchListDTO;
import com.finsight.backend.dto.response.*;

import java.util.List;


public interface WatchService {
    void insertWatch(WatchListDTO watchListDTO, String userId);

    <T extends ProductVO> void deleteWatch(Class<T> expectedType, String code, String userId);
    List<DepositByWatchDto> getWatchDepositListByUserId(String userId);
    List<FundByWatchDto> getWatchFundListByUserId(String userId);
    List<EtfByWatchDto> getWatchEtfListByUserId(String userId);
}
