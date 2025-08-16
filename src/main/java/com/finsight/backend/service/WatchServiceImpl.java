package com.finsight.backend.service;

import com.finsight.backend.domain.vo.product.*;
import com.finsight.backend.dto.NewsSentimentTotalDto;
import com.finsight.backend.dto.WatchListDTO;
import com.finsight.backend.dto.response.DepositByWatchDto;
import com.finsight.backend.dto.response.EtfByWatchDto;
import com.finsight.backend.dto.response.FundByWatchDto;
import com.finsight.backend.repository.mapper.NewsMapper;
import com.finsight.backend.repository.mapper.WatchListMapper;
import com.finsight.backend.tmptradeserverwebsocket.service.EtfPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchServiceImpl implements WatchService {
    private final WatchListMapper watchListMapper;
    private final EtfPriceService etfPriceService;
    private final NewsMapper newsMapper;


    @Override
    public int insertWatch(WatchListDTO watchListDTO, String accessToken) {
        return 0;
    }

    @Override
    public <T extends ProductVO> int deleteWatch(Class<T> expectedType, String code, String userId) {
        return 0;
    }

    @Override
    public List<DepositByWatchDto> getWatchDepositListByUserId(String userId){
        List<DepositVO> depositVOList = watchListMapper.findWatchDepositListByUserId(userId);
        return depositVOList.stream()
                .map(deposit -> {
                    DOptionVO option = deposit.getDOption().stream()
                            .findFirst()
                            .orElseThrow(null);
                    return DepositByWatchDto.depositVoToDepositByWatchDto(deposit,
                            option.getDOptionIntrRate(),
                            option.getDOptionIntrRate2());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FundByWatchDto> getWatchFundListByUserId(String userId){
        List<FundVO> fundVOList = watchListMapper.findWatchFundListByUserId(userId);
        return fundVOList.stream()
                .map(fund -> FundByWatchDto.fundVoToFundByWatchDto(fund,
                        newsSentimentPer(newsMapper.findNewsSentimentByProductCode(fund.getProductCode())),
                        etfPriceService.getPercentChangeFrom3MonthsAgo("fund_nav", fund.getProductCode()),
                        etfPriceService.getCurrent("fund_aum", fund.getProductCode()))
                )
                .collect(Collectors.toList());
    }
    @Override
    public List<EtfByWatchDto> getWatchEtfListByUserId(String userId){
        List<EtfVO> etfVOList = watchListMapper.findWatchEtfListByUserId(userId);
        return etfVOList.stream()
                .map(etf -> EtfByWatchDto.etfVoToEtfByWatchDto(etf,
                        newsSentimentPer(newsMapper.findNewsSentimentByProductCode(etf.getProductCode())),
                        etfPriceService.getCurrent("etf_nav", etf.getProductCode()))
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
