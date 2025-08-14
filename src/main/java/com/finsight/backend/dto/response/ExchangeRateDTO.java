package com.finsight.backend.dto.response;

import lombok.Getter;
import lombok.Builder;
@Getter
@Builder
public class ExchangeRateDTO {
    private String cur_unit;
    private String cur_nm;
    private String deal_bas_r;
    private String diff;
    private String percentage;
}
