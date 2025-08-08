package com.finsight.backend.mapper;

import com.finsight.backend.dto.response.SearchSuggestionResponseDTO;
import com.finsight.backend.enumerate.ProductCountry;
import com.finsight.backend.enumerate.ProductType;
import com.finsight.backend.vo.Etf;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EtfMapper {
    List<SearchSuggestionResponseDTO> findEtfNameByWord(String word);

    Etf findEtfByCode(String productCode);
    List<Etf> findEtfListByFilter(@Param("productCountry")ProductCountry productCountry,
                                  @Param("productType")ProductType productType,
                                  @Param("productRiskGrade")Integer productRiskGrade,
                                  @Param("limit") int limit,
                                  @Param("offset") int offset);
}
