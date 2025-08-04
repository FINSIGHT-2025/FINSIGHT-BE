package com.finsight.backend.websocket;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class ETFVolumeDTO {
    private String product_code;
    private BigInteger etf_volume;
    private String timestamp;
}