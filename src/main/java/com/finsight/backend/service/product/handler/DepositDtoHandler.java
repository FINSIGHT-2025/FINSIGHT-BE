package com.finsight.backend.service.product.handler;

import com.finsight.backend.common.exception.product.CustomNotFoundProduct;
import com.finsight.backend.domain.vo.user.HoldingsVO;
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
        List<DOptionVO> options = product.getDOption();
        if (options == null || options.isEmpty()) {
            throw new IllegalStateException("DOption이 비어있습니다: " + product.getProductCode());
        }

        DOptionVO option = options.stream()
                .filter(o -> "12".equals(o.getDOptionSaveTrm()))
                .findFirst()
                .orElse(options.get(0));

        return DepositDetailDto.depositVoToDepositDetailDto(
                product,
                option.getDOptionIntrRate(),
                option.getDOptionIntrRate2()
        );
    }

    @Override
    public List<ProductByFilterDto> toFilterDto(List<DepositVO> product, String userId, String sort) {
        return product.stream()
                .map(deposit -> {
                    DOptionVO option = deposit.getDOption().stream()
                            .findFirst()
                            .orElseThrow(null);
                    HoldingsVO holdingDeposit = holdingsMapper.findByUserAndProduct(userId, deposit.getProductCode());

                    Boolean userOwn = holdingDeposit == null || holdingDeposit.getHoldingsStatus().equals("zero") ? Boolean.FALSE : Boolean.TRUE;
                    return DepositByFilterDto.depositVoToDepositByFilterDto(
                            deposit,
                            option.getDOptionIntrRate(),
                            option.getDOptionIntrRate2(),
                            userOwn,
                            detailHoldingsMapper.isProductWatched(userId, deposit.getProductCode())
                    );
                })
                .collect(Collectors.toList());
    }
}
