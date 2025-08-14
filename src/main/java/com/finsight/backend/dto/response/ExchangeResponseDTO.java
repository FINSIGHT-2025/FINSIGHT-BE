package com.finsight.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ExchangeResponseDTO {
    private String displayDate;
    private List<ExchangeRateDTO> rates;
}
