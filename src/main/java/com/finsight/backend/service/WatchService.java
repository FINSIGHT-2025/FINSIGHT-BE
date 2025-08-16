package com.finsight.backend.service;

import com.finsight.backend.domain.vo.product.ProductVO;
import com.finsight.backend.dto.WatchListDTO;
import com.finsight.backend.dto.response.DepositByWatchDto;
import com.finsight.backend.dto.response.EtfByWatchDto;
import com.finsight.backend.dto.response.FundByWatchDto;

import java.util.List;


public interface WatchService {
    int insertWatch(WatchListDTO watchListDTO, String accessToken);

    <T extends ProductVO> int deleteWatch(Class<T> expectedType, String code, String userId);
    List<DepositByWatchDto> getWatchDepositListByUserId(String userId);
    List<FundByWatchDto> getWatchFundListByUserId(String userId);
    List<EtfByWatchDto> getWatchEtfListByUserId(String userId);

}
