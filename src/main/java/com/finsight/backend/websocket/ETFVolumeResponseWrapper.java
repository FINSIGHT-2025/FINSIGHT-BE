package com.finsight.backend.websocket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ETFVolumeResponseWrapper {
    private List<ETFVolumeDTO> data;
}
