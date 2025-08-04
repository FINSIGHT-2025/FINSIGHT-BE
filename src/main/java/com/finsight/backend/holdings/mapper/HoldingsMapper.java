package com.finsight.backend.holdings.mapper;

import com.finsight.backend.holdings.dto.HoldingsResponseDto;
import com.finsight.backend.holdings.vo.HistoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HoldingsMapper {

    HoldingsResponseDto selectHoldingsByUserIdAndProductCode(@Param("userId") String userId,
                                                             @Param("productCode") String productCode);

    List<HistoryVO> selectHistoriesByHoldingsId(@Param("holdingsId") Long holdingsId);

    boolean isProductWatched(@Param("userId") String userId,
                             @Param("productCode") String productCode);
}
