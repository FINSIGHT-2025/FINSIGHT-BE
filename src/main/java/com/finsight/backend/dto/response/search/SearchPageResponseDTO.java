package com.finsight.backend.dto.response.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchPageResponseDTO<T> {
    private List<T> items;
    private int page;
    private int size;
    private boolean hasNext;
    private Long totalCount;
}
