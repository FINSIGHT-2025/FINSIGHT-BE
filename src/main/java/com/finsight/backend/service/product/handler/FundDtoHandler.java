package com.finsight.backend.service.product.handler;

import com.finsight.backend.domain.vo.product.EtfVO;
import com.finsight.backend.domain.vo.user.HoldingsVO;
import com.finsight.backend.dto.NewsSentimentTotalDto;
import com.finsight.backend.dto.response.*;
import com.finsight.backend.repository.mapper.DetailHoldingsMapper;
import com.finsight.backend.repository.mapper.HoldingsMapper;
import com.finsight.backend.repository.mapper.NewsMapper;
import com.finsight.backend.tmptradeserverwebsocket.service.EtfPriceService;
import com.finsight.backend.domain.vo.product.FundVO;
import com.finsight.backend.domain.vo.news.NewsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FundDtoHandler implements ProductDtoHandler<FundVO> {
    private final NewsMapper newsMapper;
    private final HoldingsMapper holdingsMapper;
    private final DetailHoldingsMapper detailHoldingsMapper;
    private final EtfPriceService etfPriceService;

    private static Map<String, Supplier<List<FundByFilterDto>>> SORT_HANDLERS;
    @Override
    public Class<FundVO> getProductType() {
        return FundVO.class;
    }

    @Override
    public ProductDetailDto toDetailDto(FundVO product) {
        List<NewsVO> newsByProductCode = newsMapper.findNewsByProductCode(product.getProductCode());
        List<NewsResponseDTO> newsResponseDTOList = newsByProductCode.stream()
                .map(NewsResponseDTO::from)
                .toList();
        FundPriceSummaryDto fundPriceSummary = FundPriceSummaryDto.builder()
                .currentNav(etfPriceService.getCurrent("fund_nav", product.getProductCode()))
                .currentAum(etfPriceService.getCurrent("fund_aum", product.getProductCode()))
                .changeFromYesterday(etfPriceService.getChangeFromYesterday("fund_nav", product.getProductCode()))
                .percentChangeFromYesterday(etfPriceService.getPercentChangeFromYesterday("fund_nav", product.getProductCode()))
                .percentChangeFrom3MonthsAgo(etfPriceService.getPercentChangeFrom3MonthsAgo("fund_nav", product.getProductCode()))
                .build();
        return FundDetailDto.fundVoToFundDetailDto(product, newsResponseDTOList, fundPriceSummary);
    }

    @Override
    public List<ProductByFilterDto> toFilterDto(List<FundVO> product, String userId, String sort) {
        List<FundByFilterDto> fundByFilterDtoList = product.stream()
                .map((FundVO fund) -> {
                    HoldingsVO holdingDeposit = holdingsMapper.findByUserAndProduct(userId, fund.getProductCode());

                    Boolean userOwn = holdingDeposit == null || holdingDeposit.getHoldingsStatus().equals("zero") ? Boolean.FALSE : Boolean.TRUE;
                    return FundByFilterDto.fundVoToFundByFilterDto(fund,
                                    newsSentimentPer(newsMapper.findNewsSentimentByProductCode(fund.getProductCode())),
                                    userOwn,
                                    detailHoldingsMapper.isProductWatched(userId, fund.getProductCode()),
                                    etfPriceService.getPercentChangeFrom3MonthsAgo("fund_aum", fund.getProductCode()),
                                    etfPriceService.getCurrent("fund_aum", fund.getProductCode()));
                }).toList();
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
    public FundFromNews toFundFromNews(FundVO fundVO, String userId){
        return FundFromNews.fundVoToFundFromNews(
                fundVO,
                newsSentimentPer(newsMapper.findNewsSentimentByProductCode(fundVO.getProductCode())),
                holdingsMapper.existProductByUserIdAndProductCode(userId, fundVO.getProductCode()),
                detailHoldingsMapper.isProductWatched(userId, fundVO.getProductCode()),
                etfPriceService.getPercentChangeFrom3MonthsAgo("fund_aum", fundVO.getProductCode()),
                etfPriceService.getCurrent("fund_aum", fundVO.getProductCode())
        );
    }
}
