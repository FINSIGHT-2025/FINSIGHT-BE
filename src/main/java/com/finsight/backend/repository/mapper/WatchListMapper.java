package com.finsight.backend.repository.mapper;

import com.finsight.backend.domain.vo.product.DepositVO;
import com.finsight.backend.domain.vo.product.EtfVO;
import com.finsight.backend.domain.vo.product.FundVO;
import com.finsight.backend.dto.WatchListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WatchListMapper {
    // 찜한 상품 등록
    int insertWatch(WatchListDTO watchListDTO);

    // 찜한 상품 해제
    int deleteWatch(@Param("userId") String userId,
                    @Param("productCode") String productCode,
                    @Param("productCategory") String productCategory);

    boolean checkProductExists(@Param("productCode") String productCode,
                          @Param("productCategory") String productCategory);

    List<DepositVO> findWatchDepositListByUserId(String userId);
    List<FundVO> findWatchFundListByUserId(String userId);
    List<EtfVO> findWatchEtfListByUserId(String userId);
}
