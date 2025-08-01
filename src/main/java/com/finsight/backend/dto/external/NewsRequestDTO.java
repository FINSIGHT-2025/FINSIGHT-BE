package com.finsight.backend.dto.external;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class NewsRequestDTO {
    private List<String> symbols;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    @Builder.Default
    private String order = "score";  // 기본값 고정

    @Builder.Default
    private Integer pageSize = 100;   // 기본값 20


}
