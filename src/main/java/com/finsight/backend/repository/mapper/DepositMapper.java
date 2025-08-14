package com.finsight.backend.repository.mapper;

import com.finsight.backend.dto.response.SearchSuggestionResponseDTO;
import com.finsight.backend.domain.vo.product.DepositVO;

import java.util.List;

public interface DepositMapper {

    List<SearchSuggestionResponseDTO> findDepositNameByWord(String word);

    /**
     *
     * @param productCode
     * @return DepositVO
     */
    DepositVO findDepositByCode(String productCode);

    /**
     *
     * @return List<DepositVO> -> OrderByIntrRate(기본 금리)
     */
    List<DepositVO> findDepositListOrderByIntrRate();

    /**
     *
     * @return List<DepositVO> -> OrderByIntrRate2(최고 금리)
     */
    List<DepositVO> findDepositListOrderByIntrRate2();

}
