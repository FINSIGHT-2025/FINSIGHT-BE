package com.finsight.backend.tmptradeserverwebsocket.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EtfVolumeDTO {
    private String productCode;
    private Long etfVolume;
    private String timestamp;
}