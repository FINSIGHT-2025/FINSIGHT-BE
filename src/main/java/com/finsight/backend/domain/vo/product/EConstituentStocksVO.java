package com.finsight.backend.domain.vo.product;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class EConstituentStocksVO {
    private Integer eConstituentStocksId;
    private String eConstituentStocksName;
    private Double eConstituentPer;
}
