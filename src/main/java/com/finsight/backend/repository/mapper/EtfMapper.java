package com.finsight.backend.repository.mapper;

import com.finsight.backend.dto.response.NewsEtfDTO;
import com.finsight.backend.dto.response.search.SearchSuggestionResponseDTO;
import com.finsight.backend.domain.enumerate.ProductCountry;
import com.finsight.backend.domain.enumerate.ProductType;
import com.finsight.backend.domain.vo.product.EtfVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EtfMapper {
    EtfVO findEtfByCode(String productCode);
    List<EtfVO> findEtfListByFilter(@Param("productCountry")ProductCountry productCountry,
                                    @Param("productType")ProductType productType,
                                    @Param("productRiskGrade")Integer[] productRiskGrade,
                                    @Param("limit") Integer limit,
                                    @Param("offset") Integer offset);

    NewsEtfDTO findEtfByProductCode(String productCode);
}
