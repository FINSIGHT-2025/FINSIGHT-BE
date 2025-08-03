package com.finsight.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EtfByFilterDto extends ProductByFilterDto{
    String country;
    String etfType;
    Integer nav;
    Integer volume;
    Integer rateOfReturn;
}
