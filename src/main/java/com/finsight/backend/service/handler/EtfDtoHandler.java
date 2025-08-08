package com.finsight.backend.service.handler;

import com.finsight.backend.dto.NewsSentimentDto;
import com.finsight.backend.dto.response.*;
import com.finsight.backend.mapper.NewsMapper;
import com.finsight.backend.vo.Etf;
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
public class EtfDtoHandler implements ProductDtoHandler<Etf>{
    private final NewsMapper newsMapper;
    @Override
    public Class<Etf> getProductType() {
        return Etf.class;
    }

    @Override
    public ProductDetailDto toDetailDto(Etf product) {
        List<NewsVO> newsByProductCode = newsMapper.findNewsByProductCode(product.getProductCode());
        List<NewsResponseDTO> newsResponseDTOList = newsByProductCode.stream()
                .map(NewsResponseDTO::from)
                .toList();
        return EtfDetailDetailDto.etfVoToEtfDetailDto(product, newsResponseDTOList);
    }

    @Override
    public List<ProductByFilterDto> toFilterDto(List<Etf> product) {
        return product.stream()
                .map((Etf etf) -> EtfByFilterDto.etfVoToEtfByFilterDto(etf,
                        newsSentimentPer(newsMapper.findNewsSentimentByProductCode(etf.getProductCode()))))
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
