package com.finsight.backend.service.handler;

import com.finsight.backend.dto.NewsSentimentTotalDto;
import com.finsight.backend.dto.response.*;
import com.finsight.backend.repository.mapper.DetailHoldingsMapper;
import com.finsight.backend.repository.mapper.HoldingsMapper;
import com.finsight.backend.repository.mapper.NewsMapper;
import com.finsight.backend.domain.vo.product.EtfVO;
import com.finsight.backend.domain.vo.news.NewsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EtfDtoHandler implements ProductDtoHandler<EtfVO>{
    private final NewsMapper newsMapper;
    private final HoldingsMapper holdingsMapper;
    private final DetailHoldingsMapper detailHoldingsMapper;
    @Override
    public Class<EtfVO> getProductType() {
        return EtfVO.class;
    }

    @Override
    public ProductDetailDto toDetailDto(EtfVO product) {
        List<NewsVO> newsByProductCode = newsMapper.findNewsByProductCode(product.getProductCode());
        List<NewsResponseDTO> newsResponseDTOList = newsByProductCode.stream()
                .map(NewsResponseDTO::from)
                .toList();
        return EtfDetailDetailDto.etfVoToEtfDetailDto(product, newsResponseDTOList);
    }

    @Override
    public List<ProductByFilterDto> toFilterDto(List<EtfVO> product, String userId, String sort) {
        return product.stream()
                .map((EtfVO etf) -> EtfByFilterDto.etfVoToEtfByFilterDto(etf,
                        newsSentimentPer(newsMapper.findNewsSentimentByProductCode(etf.getProductCode())),
                        holdingsMapper.existProductByUserIdAndProductCode(userId, etf.getProductCode()),
                        detailHoldingsMapper.isProductWatched(userId, etf.getProductCode())
                        )
                )
                .collect(Collectors.toList());
    }

    public NewsSentimentTotalDto newsSentimentPer(List<String> newsSentimentList){
        Map<String, Long> sentimentCnt = newsSentimentList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        int total = newsSentimentList.size();
        if(total == 0) return null;

        Function<String, Integer> getPercent = sentiment ->
                (int) (sentimentCnt.getOrDefault(sentiment, 0L) * 100 / total);

        return NewsSentimentTotalDto.builder()
                .positive(getPercent.apply("positive"))
                .negative(getPercent.apply("negative"))
                .neutral(getPercent.apply("neutral"))
                .build();
    }
}
