package com.finsight.backend.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeApiResponseDTO {
    @JsonProperty("cur_unit")
    private String curUnit;

    @JsonProperty("cur_nm")
    private String curNm;

    @JsonProperty("deal_bas_r")
    private String dealBasR;
}
