package com.finsight.backend.domain.vo.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class PriceVolumePointVO {
    private Instant time;
    private Double price;
    private Long volume;
}
