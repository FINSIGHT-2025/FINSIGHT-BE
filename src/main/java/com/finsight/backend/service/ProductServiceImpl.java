package com.finsight.backend.service;

import com.finsight.backend.detailhodings.vo.DetailHistoryVO;
import com.finsight.backend.detailhodings.dto.DetailHoldingsResponseDto;
import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.mapper.DetailHoldingsMapper;
import com.finsight.backend.mapper.UserMapper;
import com.finsight.backend.service.handler.ProductDtoHandler;
import com.finsight.backend.service.handler.ProductVoHandler;
import com.finsight.backend.vo.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{
    private final List<ProductVoHandler<? extends Product>> voHandlers;
    private final List<ProductDtoHandler<? extends Product>> dtoHandlers;
    private final DetailHoldingsMapper detailHoldingsMapper;

    public ProductServiceImpl(List<ProductVoHandler<? extends Product>> voHandlers, 
                            List<ProductDtoHandler<? extends Product>> dtoHandlers,
                            DetailHoldingsMapper detailHoldingsMapper) {
        this.voHandlers = voHandlers;
        this.dtoHandlers = dtoHandlers;
        this.detailHoldingsMapper = detailHoldingsMapper;
    }

    @Override
    public <T extends Product> ProductDetailDto findProduct(String productCode, Class<T> expectedType, String userId){
        T productVo =  voHandlers.stream()
                .filter(handler -> handler.getProductType().equals(expectedType))
                .findFirst()
                .map(handler -> expectedType.cast(handler.findProduct(productCode)))
                .orElseThrow(() -> new IllegalArgumentException("해당 vo 핸들러 없음 : " + expectedType));

        ProductDtoHandler<? extends Product> matchedVoHandler = dtoHandlers.stream()
                .filter(handler -> handler.getProductType().equals(expectedType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 dto 핸들러 없음 : " + expectedType));

        @SuppressWarnings("unchecked")
        ProductDtoHandler<T> dtoHandler = (ProductDtoHandler<T>) matchedVoHandler;

        ProductDetailDto dto = dtoHandler.toDetailDto(productVo);

        // holdings 정보
        DetailHoldingsResponseDto holdings = detailHoldingsMapper.selectHoldingsByUserIdAndProductCode(userId, productCode);
        if (holdings != null) {
            Long holdingsId = holdings.getHoldingsId();

            List<DetailHistoryVO> histories = detailHoldingsMapper.selectHistoriesByHoldingsId(holdingsId);
            holdings.setHistory(histories);

            boolean isWatched = detailHoldingsMapper.isProductWatched(userId, productCode);
            holdings.setWatched(isWatched);
        }

        dto.setHoldings(holdings);

        return dto;
    }

    @Override
    public <T extends Product> List<ProductByFilterDto> findProductByFilter(Class<T> expectedType,
                                                                            String sort,
                                                                            String country,
                                                                            String type,
                                                                            String userId,
                                                                            Boolean isMatched) {

        @SuppressWarnings("unchecked")
        ProductVoHandler<T> matchedVoHandler = (ProductVoHandler<T>) voHandlers.stream()
                .filter(handler -> handler.getProductType().equals(expectedType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 vo 핸들러 없음 : " + expectedType));

        List<T> productList = matchedVoHandler.findProductListByFilter(sort, country, type, isMatched, userId);


        @SuppressWarnings("unchecked")
        ProductDtoHandler<T> productDtoHandler =  (ProductDtoHandler<T>) dtoHandlers.stream()
                .filter(handler -> handler.getProductType().equals(expectedType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 dto 핸들러 없음 : " + expectedType));

        return productDtoHandler.toFilterDto(productList, userId, sort);
    }
}
