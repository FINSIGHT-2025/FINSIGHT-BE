package com.finsight.backend.dto.response.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchSuggestionResponseDTO {
    private String productCode;
    private String productName;
    private String type;
}
