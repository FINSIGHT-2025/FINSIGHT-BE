package com.finsight.backend.service;

import com.finsight.backend.dto.response.KeywordResponseDTO;
import com.finsight.backend.dto.response.NewsByKeywordResponseDTO;
import com.finsight.backend.dto.response.NewsResponseDTO;
import com.finsight.backend.mapper.NewsMapper;
import com.finsight.backend.vo.NewsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsMapper newsMapper;

    public List<KeywordResponseDTO> getTopKeywords() {
        return newsMapper.findTopKeywords();
    }

    public NewsByKeywordResponseDTO getNewsByKeywordId(Long keywordId) {
        // 1. 키워드 ID로 뉴스 목록(VO)을 바로 조회
        List<NewsVO> newsVOs = newsMapper.findNewsByKeywordId(keywordId);

        // 2. VO 리스트를 DTO 리스트로 변환
        List<NewsResponseDTO> newsDtos = newsVOs.stream()
                .map(NewsResponseDTO::from)
                .collect(Collectors.toList());

        // 3. DTO에 뉴스 리스트를 담아 반환
        return new NewsByKeywordResponseDTO(newsDtos);
    }

    public List<NewsResponseDTO> getNewsByProductCode(String productCode) {
        // 상품 코드로 뉴스 목록(VO) 조회
        List<NewsVO> newsVOs = newsMapper.findNewsByProductCode(productCode);

        // 조회된 NewsVO 리스트를 NewsResponseDto 리스트로 변환
        return newsVOs.stream()
                .map(NewsResponseDTO::from)
                .collect(Collectors.toList());
    }
}
