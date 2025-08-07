package com.finsight.backend.tradeserverwebsocket.dto;

import lombok.Data;

@Data
public class EtfUpdateDTO {
    private String productCode;
    private double priceNow;
    private long volumeNow;
}
