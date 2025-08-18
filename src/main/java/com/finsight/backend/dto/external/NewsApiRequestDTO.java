package com.finsight.backend.dto.external;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Data
@Builder
public class NewsApiRequestDTO {
    private String symbols;

    @Builder.Default
    private List<String> keywords = Collections.emptyList();

    private LocalDate dateFrom;
    private LocalDate dateTo;

    @Builder.Default
    private String order = "score";  // 기본값 고정

    @Builder.Default
    private Integer pageSize = 10;   // 기본값 10


}
