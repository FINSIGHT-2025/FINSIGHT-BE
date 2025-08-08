package com.finsight.backend.service.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsight.backend.dto.NewsSentimentDto;
import com.finsight.backend.dto.response.*;
import com.finsight.backend.mapper.NewsMapper;
import com.finsight.backend.vo.Fund;
import com.finsight.backend.vo.NewsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FundDtoHandler implements ProductDtoHandler<Fund> {
    private final NewsMapper newsMapper;
    @Override
    public Class<Fund> getProductType() {
        return Fund.class;
    }

    @Override
    public ProductDetailDto toDetailDto(Fund product) {
        List<NewsVO> newsByProductCode = newsMapper.findNewsByProductCode(product.getProductCode());
        List<NewsResponseDTO> newsResponseDTOList = newsByProductCode.stream()
                .map(NewsResponseDTO::from)
                .toList();
        return FundDetailDetailDto.fundVoToFundDetailDto(product, newsResponseDTOList);
    }

    @Override
    public List<ProductByFilterDto> toFilterDto(List<Fund> product) {
        return product.stream()
                .map((Fund fund) -> FundByFilterDto.fundVoToFundByFilterDto(fund,
                        newsSentimentPer(newsMapper.findNewsSentimentByProductCode(fund.getProductCode()))))
                .collect(Collectors.toList());
    }
    public NewsSentimentDto newsSentimentPer(List<String> newsSentimentList){
        Map<String, Long> sentimentCnt = newsSentimentList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        int total = newsSentimentList.size();
        if(total == 0) return null;

        Function<String, Integer> getPercent = sentiment ->
                (int) (sentimentCnt.getOrDefault(sentiment, 0L) * 100 / total);

        return NewsSentimentDto.builder()
                .positive(getPercent.apply("positive"))
                .negative(getPercent.apply("negative"))
                .neutral(getPercent.apply("neutral"))
                .build();
    }
}
