package com.finsight.backend.service;

import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.service.handler.ProductDtoHandler;
import com.finsight.backend.service.handler.ProductVoHandler;
import com.finsight.backend.vo.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{
    private final List<ProductVoHandler<? extends Product>> voHandlers;
    private final List<ProductDtoHandler<? extends Product>> dtoHandlers;

    public ProductServiceImpl(List<ProductVoHandler<? extends Product>> voHandlers, List<ProductDtoHandler<? extends Product>> dtoHandlers) {
        this.voHandlers = voHandlers;
        this.dtoHandlers = dtoHandlers;
    }

    @Override
    public <T extends Product> ProductDetailDto findProduct(String productCode, Class<T> expectedType){
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

        return dtoHandler.toDetailDto(productVo);
    }

    @Override
    public <T extends Product> List<ProductByFilterDto> findProductByFilter(Class<T> expectedType, String sort, String country, String type, Integer riskGrade) {

        @SuppressWarnings("unchecked")
        ProductVoHandler<T> matchedVoHandler = (ProductVoHandler<T>) voHandlers.stream()
                .filter(handler -> handler.getProductType().equals(expectedType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 vo 핸들러 없음 : " + expectedType));

        List<T> productList = matchedVoHandler.findProductListByFilter(sort, country, type, riskGrade);


        @SuppressWarnings("unchecked")
        ProductDtoHandler<T> productDtoHandler =  (ProductDtoHandler<T>) dtoHandlers.stream()
                .filter(handler -> handler.getProductType().equals(expectedType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 dto 핸들러 없음 : " + expectedType));

        return productDtoHandler.toFilterDto(productList);
    }
}
