package com.finsight.backend.service.product.handler;

import com.finsight.backend.domain.vo.user.HoldingsVO;
import com.finsight.backend.dto.NewsSentimentTotalDto;
import com.finsight.backend.dto.response.*;
import com.finsight.backend.repository.mapper.DetailHoldingsMapper;
import com.finsight.backend.repository.mapper.HoldingsMapper;
import com.finsight.backend.repository.mapper.NewsMapper;
import com.finsight.backend.domain.vo.product.EtfVO;
import com.finsight.backend.domain.vo.news.NewsVO;
import com.finsight.backend.tmptradeserverwebsocket.service.EtfPriceService;
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
    private final EtfPriceService etfPriceService;

//     private static Map<String, Supplier<List<EtfByFilterDto>>> SORT_HANDLERS;
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
        return EtfDetailDto.etfVoToEtfDetailDto(product, newsResponseDTOList);
    }

    @Override
    public List<ProductByFilterDto> toFilterDto(List<EtfVO> product, String userId, String sort) {
        List<EtfByFilterDto> etfByFilterDtoList = product.stream()
                .map((EtfVO etf) -> {
                    HoldingsVO holdingDeposit = holdingsMapper.findByUserAndProduct(userId, etf.getProductCode());

                    Boolean userOwn = holdingDeposit == null || holdingDeposit.getHoldingsStatus().equals("zero") ? Boolean.FALSE : Boolean.TRUE;

                    return EtfByFilterDto.etfVoToEtfByFilterDto(etf,
                                    newsSentimentPer(newsMapper.findNewsSentimentByProductCode(etf.getProductCode())),
                                    userOwn,
                                    detailHoldingsMapper.isProductWatched(userId, etf.getProductCode()),
                                    etfPriceService.getCurrent("etf_nav", etf.getProductCode())
                            );
                        }
                ).toList();
        // SORT_HANDLERS = Map.of(
        //         "asc", () -> etfByFilterDtoList.stream()
        //                 .sorted(Comparator.comparing(EtfByFilterDto::getEtfNav).reversed())
        //                 .toList(),
        //         "desc", () -> etfByFilterDtoList.stream()
        //                 .sorted(Comparator.comparing(EtfByFilterDto::getEtfNav))
        //                 .toList()
        // );
        // return SORT_HANDLERS.get(sort).get().stream()
        return etfByFilterDtoList.stream()
                .map(etf -> (ProductByFilterDto) etf)
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

    public EtfFromNews toEtfFromNews(EtfVO etfVO, String userId){
        return EtfFromNews.etfVoToEtfFromNews(
                etfVO,
                newsSentimentPer(newsMapper.findNewsSentimentByProductCode(etfVO.getProductCode())),
                holdingsMapper.existProductByUserIdAndProductCode(userId, etfVO.getProductCode()),
                detailHoldingsMapper.isProductWatched(userId, etfVO.getProductCode()),
                etfPriceService.getCurrent("etf_nav", etfVO.getProductCode())
                );
    }
}
