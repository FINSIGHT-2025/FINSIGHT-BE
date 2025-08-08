package com.finsight.backend.service.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsight.backend.dto.NewsSentimentDto;
import com.finsight.backend.dto.response.*;
import com.finsight.backend.mapper.DetailHoldingsMapper;
import com.finsight.backend.mapper.HoldingsMapper;
import com.finsight.backend.mapper.NewsMapper;
import com.finsight.backend.tradeserverwebsocket.service.EtfPriceService;
import com.finsight.backend.vo.Fund;
import com.finsight.backend.vo.NewsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FundDtoHandler implements ProductDtoHandler<Fund> {
    private final NewsMapper newsMapper;
    private final HoldingsMapper holdingsMapper;
    private final DetailHoldingsMapper detailHoldingsMapper;
    private final EtfPriceService etfPriceService;

    private static Map<String, Supplier<List<FundByFilterDto>>> SORT_HANDLERS;
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
    public List<ProductByFilterDto> toFilterDto(List<Fund> product, String userId, String sort) {
        List<FundByFilterDto> fundByFilterDtoList = product.stream()
                .map((Fund fund) -> FundByFilterDto.fundVoToFundByFilterDto(fund,
                        newsSentimentPer(newsMapper.findNewsSentimentByProductCode(fund.getProductCode())),
                        holdingsMapper.existProductByUserIdAndProductCode(userId, fund.getProductCode()),
                        detailHoldingsMapper.isProductWatched(userId, fund.getProductCode()),
                        etfPriceService.getPercentChangeFrom3MonthsAgo("fund_aum", fund.getProductCode()),
                        etfPriceService.getCurrent("fund_aum", fund.getProductCode()))
                )
                .toList();
        SORT_HANDLERS = Map.of(
                "rate_of_return", () -> fundByFilterDtoList.stream()
                        .sorted(Comparator.comparing(FundByFilterDto::getProductRateOfReturn).reversed())
                        .toList(),
                "fund_scale", () -> fundByFilterDtoList.stream()
                        .sorted(Comparator.comparing(FundByFilterDto::getFundScale).reversed())
                        .toList()
        );
        return SORT_HANDLERS.get(sort).get().stream()
                .map(fund -> (ProductByFilterDto) fund)
                .toList();
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
