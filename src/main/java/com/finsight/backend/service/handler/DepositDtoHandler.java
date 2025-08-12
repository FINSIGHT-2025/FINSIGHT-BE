package com.finsight.backend.service.handler;

import com.finsight.backend.dto.response.DepositByFilterDto;
import com.finsight.backend.dto.response.DepositDetailDto;
import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.repository.mapper.DetailHoldingsMapper;
import com.finsight.backend.repository.mapper.HoldingsMapper;
import com.finsight.backend.domain.vo.product.DOptionVO;
import com.finsight.backend.domain.vo.product.DepositVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class DepositDtoHandler implements ProductDtoHandler<DepositVO>{
    private final HoldingsMapper holdingsMapper;
    private final DetailHoldingsMapper detailHoldingsMapper;
    @Override
    public Class<DepositVO> getProductType() {
        return DepositVO.class;
    }

    @Override
    public ProductDetailDto toDetailDto(DepositVO product) {
        return DepositDetailDto.depositVoToDepositDetailDto(product);
    }

    @Override
    public List<ProductByFilterDto> toFilterDto(List<DepositVO> product, String userId, String sort) {
        return product.stream()
                .map(deposit -> {
                    DOptionVO option = deposit.getDOption().stream()
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("DOption이 비어있습니다: " + deposit.getProductCode()));
                    return DepositByFilterDto.depositVoToDepositByFilterDto(
                            deposit,
                            option.getDOptionIntrRate(),
                            option.getDOptionIntrRate2(),
                            holdingsMapper.existProductByUserIdAndProductCode(userId, deposit.getProductCode()),
                            detailHoldingsMapper.isProductWatched(userId, deposit.getProductCode())
                    );
                })
                .collect(Collectors.toList());
    }
}
