package com.finsight.backend.domain.enumerate;

import com.finsight.backend.domain.vo.product.DepositVO;
import com.finsight.backend.domain.vo.product.EtfVO;
import com.finsight.backend.domain.vo.product.FundVO;
import com.finsight.backend.domain.vo.product.ProductVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum WatchListId implements BaseEnum {
//`product_category` ENUM('deposit', 'fund', 'etf') NOT NULL COMMENT '카테고리',
    DEPOSIT("deposit", DepositVO.class),
    FUND("fund", FundVO.class),
    ETF("etf", EtfVO.class);

    private final String dbValue;
    private final Class<? extends ProductVO> productType;

    @Override
    public String toString() {
        return dbValue;
    }
    public static WatchListId fromDbValue(String dbValue) {
        for (WatchListId type : values()) {
            if (type.dbValue.equals(dbValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown type: " + dbValue);
    }

    public static WatchListId fromType(Class<? extends ProductVO> type) {
        return Arrays.stream(values())
                .filter(category -> category.getProductType().isAssignableFrom(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 상품 타입입니다: " + type.getSimpleName()));
    }
}
