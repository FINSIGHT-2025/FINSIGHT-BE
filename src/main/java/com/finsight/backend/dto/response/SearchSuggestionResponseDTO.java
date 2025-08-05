package com.finsight.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchSuggestionResponseDTO {
    private Long productCode;
    private String productName;
    private String type;
}
