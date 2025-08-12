package com.finsight.backend.tradeserverwebsocket.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EtfPriceDTO {
    private String productCode;
    private Double etfPrice;
    private String timestamp;
}