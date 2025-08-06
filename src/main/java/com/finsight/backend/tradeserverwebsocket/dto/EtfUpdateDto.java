package com.finsight.backend.tradeserverwebsocket.dto;

import lombok.Data;

@Data
public class EtfUpdateDto {
    private String productCode;
    private double priceNow;
    private double volumeNow;
}
