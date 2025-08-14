package com.finsight.backend.repository.mapper;

import com.finsight.backend.dto.response.NewsFundDTO;
import com.finsight.backend.domain.enumerate.ProductCountry;
import com.finsight.backend.domain.enumerate.ProductType;
import com.finsight.backend.domain.vo.product.FundVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.finsight.backend.dto.response.search.SearchSuggestionResponseDTO;

import java.util.List;
@Mapper
public interface FundMapper {
    FundVO findFundByCode(String productCode);
    List<FundVO> findFundListByFilter(@Param("productCountry") ProductCountry productCountry,
                                      @Param("productType")ProductType productType,
                                      @Param("productRiskGrade") Integer[] productRiskGrade);

    List<FundVO> selectAllFundStock();
    NewsFundDTO findFundByProductCode(String productCode);
}