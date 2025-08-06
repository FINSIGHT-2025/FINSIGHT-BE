package com.finsight.backend.tradeserverwebsocket.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class ETFVolumeDTO {
    private String product_code;
    private int etf_volume;
    private String timestamp;
}