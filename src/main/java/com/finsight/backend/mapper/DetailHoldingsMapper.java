package com.finsight.backend.mapper;

import com.finsight.backend.detailhodings.dto.DetailHoldingsResponseDto;
import com.finsight.backend.detailhodings.vo.DetailHistoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DetailHoldingsMapper {

    DetailHoldingsResponseDto selectHoldingsByUserIdAndProductCode(@Param("userId") String userId,
                                                                   @Param("productCode") String productCode);

    List<DetailHistoryVO> selectHistoriesByHoldingsId(@Param("holdingsId") Long holdingsId);

    Boolean isProductWatched(@Param("userId") String userId,
                             @Param("productCode") String productCode);
}
