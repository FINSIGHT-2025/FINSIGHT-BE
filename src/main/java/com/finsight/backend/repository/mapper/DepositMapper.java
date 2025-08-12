package com.finsight.backend.repository.mapper;

import com.finsight.backend.dto.response.SearchSuggestionResponseDTO;
import com.finsight.backend.domain.vo.product.DepositVO;

import java.util.List;

public interface DepositMapper {
    List<SearchSuggestionResponseDTO> findDepositNameByWord(String word);
    DepositVO findDepositByCode(String productCode);
    List<DepositVO> findDepositListOrderByIntrRate();
    List<DepositVO> findDepositListOrderByIntrRate2();

}
