package com.finsight.backend.tradeserverwebsocket.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finsight.backend.tradeserverwebsocket.dto.ETFVolumeDTO;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ETFVolumeResponseWrapper {
    private List<ETFVolumeDTO> data;
}
