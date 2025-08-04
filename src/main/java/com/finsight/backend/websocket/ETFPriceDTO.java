package com.finsight.backend.websocket;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ETFPriceDTO {
    private String product_code;
    private double etf_price;
    private String timestamp;
}