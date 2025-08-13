package com.finsight.backend.service.news;

import com.finsight.backend.domain.vo.news.NewsProductVO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewsProductSelector {
    public List<NewsProductVO> recommendTop3(List<NewsProductVO> candidates) {
        // 앞에서 3개 자르기
        return candidates.stream()
                .limit(10)
                .collect(Collectors.toList());

        /*
        // [개인화 로직]
        // 사용자 정보를 기반으로 점수를 매겨 정렬 후 3개 선택
        return candidates.stream()
                .sorted((p1, p2) -> calculateScore(p2, user) - calculateScore(p1, user))
                .limit(3)
                .collect(Collectors.toList());
        */
    }

    // 개인화 점수 계산 로직
    private int calculateScore(NewsProductVO product) {
        // 개인화 점수 계산 로직 ...
        return 0;
    }
}
