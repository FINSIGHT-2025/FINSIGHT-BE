package com.finsight.backend.service.product;

import com.finsight.backend.common.exception.product.CustomNotFoundProduct;
import com.finsight.backend.tmpdetailhodings.vo.DetailHistoryVO;
import com.finsight.backend.tmpdetailhodings.dto.DetailHoldingsResponseDto;
import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.repository.mapper.DetailHoldingsMapper;
import com.finsight.backend.service.product.handler.ProductDtoHandler;
import com.finsight.backend.service.product.handler.ProductVoHandler;
import com.finsight.backend.domain.vo.product.ProductVO;
import com.finsight.backend.domain.vo.product.DepositVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{
    private final List<ProductVoHandler<? extends ProductVO>> voHandlers;
    private final List<ProductDtoHandler<? extends ProductVO>> dtoHandlers;
    private final DetailHoldingsMapper detailHoldingsMapper;

    public ProductServiceImpl(List<ProductVoHandler<? extends ProductVO>> voHandlers,
                            List<ProductDtoHandler<? extends ProductVO>> dtoHandlers,
                            DetailHoldingsMapper detailHoldingsMapper) {
        this.voHandlers = voHandlers;
        this.dtoHandlers = dtoHandlers;
        this.detailHoldingsMapper = detailHoldingsMapper;
    }

    @Override
    public <T extends ProductVO> ProductDetailDto findProduct(String productCode, Class<T> expectedType, String userId){
        T productVo =  voHandlers.stream()
                .filter(handler -> handler.getProductType().equals(expectedType))
                .findFirst()
                .map(handler -> expectedType.cast(handler.findProduct(productCode)))
                .orElseThrow(CustomNotFoundProduct::new);

        ProductDtoHandler<? extends ProductVO> matchedVoHandler = dtoHandlers.stream()
                .filter(handler -> handler.getProductType().equals(expectedType))
                .findFirst()
                .orElseThrow(CustomNotFoundProduct::new);

        @SuppressWarnings("unchecked")
        ProductDtoHandler<T> dtoHandler = (ProductDtoHandler<T>) matchedVoHandler;

        ProductDetailDto dto = dtoHandler.toDetailDto(productVo);

        // holdings 정보
        DetailHoldingsResponseDto holdings = detailHoldingsMapper.selectHoldingsByUserIdAndProductCode(userId, productCode);
        if (holdings != null) {
            Long holdingsId = holdings.getHoldingsId();

            List<DetailHistoryVO> histories;
            
            // Deposit 상품일 경우 buy 타입이면서 가장 최근인 history만 가져오기
            if (expectedType.equals(DepositVO.class)) {
                histories = detailHoldingsMapper.selectLatestBuyHistoryByHoldingsId(holdingsId);
            } else {
                // 다른 상품들은 모든 history 가져오기
                histories = detailHoldingsMapper.selectHistoriesByHoldingsId(holdingsId);
            }
            
            holdings.setHistory(histories);

            Boolean isWatched = detailHoldingsMapper.isProductWatched(userId, productCode);
            holdings.setWatched(isWatched);
        }

        dto.setHoldings(holdings);

        return dto;
    }

    @Override
    public <T extends ProductVO> List<ProductByFilterDto> findProductByFilter(Class<T> expectedType,
                                                                              String sort,
                                                                              String country,
                                                                              String type,
                                                                              String userId,
                                                                              Boolean isMatched,
                                                                              Integer limit,
                                                                              Integer offset) {

        @SuppressWarnings("unchecked")
        ProductVoHandler<T> matchedVoHandler = (ProductVoHandler<T>) voHandlers.stream()
                .filter(handler -> handler.getProductType().equals(expectedType))
                .findFirst()
                .orElseThrow(CustomNotFoundProduct::new);

        List<T> productList = matchedVoHandler.findProductListByFilter(sort, country, type, isMatched, userId, limit, offset);


        @SuppressWarnings("unchecked")
        ProductDtoHandler<T> productDtoHandler =  (ProductDtoHandler<T>) dtoHandlers.stream()
                .filter(handler -> handler.getProductType().equals(expectedType))
                .findFirst()
                .orElseThrow(CustomNotFoundProduct::new);

        return productDtoHandler.toFilterDto(productList, userId, sort);
    }
}
