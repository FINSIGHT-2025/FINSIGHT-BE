package com.finsight.backend.tmptradeserverwebsocket.dto;

import lombok.Data;

@Data
public class EtfUpdateDTO {
    private String productCode;
    private Double priceNow;
    private Long volumeNow;
}
