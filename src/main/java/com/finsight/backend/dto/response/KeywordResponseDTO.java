package com.finsight.backend.dto.response;

import com.finsight.backend.vo.KeywordVO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KeywordResponseDTO {
    private Long keywordId;
    private String keyword;
    private int count;

    public static KeywordResponseDTO from(KeywordVO vo) {
        return KeywordResponseDTO.builder()
                .keywordId(vo.getKeywordId())
                .keyword(vo.getKeyword())
                .build();
    }
}