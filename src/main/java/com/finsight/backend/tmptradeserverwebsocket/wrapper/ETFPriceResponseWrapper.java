package com.finsight.backend.tmptradeserverwebsocket.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finsight.backend.tmptradeserverwebsocket.dto.EtfPriceDTO;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ETFPriceResponseWrapper {
    private List<EtfPriceDTO> data;
}