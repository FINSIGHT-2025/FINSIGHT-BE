package com.finsight.backend.service;

import com.finsight.backend.common.exception.BusinessException;
import com.finsight.backend.common.exception.ErrorCode;
import com.finsight.backend.domain.enumerate.WatchListId;
import com.finsight.backend.domain.vo.product.*;
import com.finsight.backend.domain.vo.user.HoldingsVO;
import com.finsight.backend.domain.vo.product.WatchVO;
import com.finsight.backend.dto.WatchListDTO;
import com.finsight.backend.dto.NewsSentimentTotalDto;
import com.finsight.backend.dto.response.*;
import com.finsight.backend.repository.mapper.WatchListMapper;
import com.finsight.backend.repository.mapper.DetailHoldingsMapper;
import com.finsight.backend.repository.mapper.HoldingsMapper;
import com.finsight.backend.repository.mapper.NewsMapper;
import com.finsight.backend.tmptradeserverwebsocket.service.EtfPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchServiceImpl implements WatchService {
    private final WatchListMapper watchListMapper;
    private final DetailHoldingsMapper detailHoldingsMapper;
    private final HoldingsMapper holdingsMapper;
    private final NewsMapper newsMapper;
    private final EtfPriceService etfPriceService;

    @Override
    @Transactional
    public void insertWatch(WatchListDTO watchListDTO, String userId) {
        watchListDTO.setUserId(userId);
        Boolean isWatched = detailHoldingsMapper.isProductWatched(
            userId, watchListDTO.getProductCode()
        );
        if (Boolean.TRUE.equals(isWatched)) {
            throw new BusinessException(ErrorCode.DUPLICATE_WATCH);
        }
        
        String productCategory = watchListDTO.getProductCategory();
        boolean productExists = watchListMapper.checkProductExists(
            watchListDTO.getProductCode(), productCategory
        );
        
        if (!productExists) {
            throw new BusinessException(ErrorCode.NOT_FOUND_PRODUCT);
        }
        
        try {
            watchListMapper.insertWatch(watchListDTO);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            throw new BusinessException(ErrorCode.DUPLICATE_WATCH);
        } catch (org.springframework.dao.DataAccessException e) {
            throw new BusinessException(ErrorCode.WATCH_INSERT_FAIL);
        }
    }

    @Override
    @Transactional
    public <T extends ProductVO> void deleteWatch(Class<T> expectedType, String code, String userId) {
        WatchListId category = WatchListId.fromType(expectedType);
        try {
            int result = watchListMapper.deleteWatch(userId, code, category.getDbValue());
            if (result <= 0) {
                throw new BusinessException(ErrorCode.WATCH_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.WATCH_DELETE_FAIL);
        }
    }

    @Override
    public List<DepositByWatchDto> getWatchDepositListByUserId(String userId){
        List<DepositVO> depositVOList = watchListMapper.findWatchDepositListByUserId(userId);
        return depositVOList.stream()
                .map(deposit -> {
                    return getDepositObject(userId, deposit);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FundByWatchDto> getWatchFundListByUserId(String userId){
        List<FundVO> fundVOList = watchListMapper.findWatchFundListByUserId(userId);
        return fundVOList.stream()
                .map(fund -> {
                    return getFundObject(userId, fund);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EtfByWatchDto> getWatchEtfListByUserId(String userId){
        List<EtfVO> etfVOList = watchListMapper.findWatchEtfListByUserId(userId);
        return etfVOList.stream()
                .map(etf -> {
                    return getEtfObject(userId, etf);
                })
                .collect(Collectors.toList());
    }

    private NewsSentimentTotalDto newsSentimentPer(List<String> newsSentimentList){
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

    @Override
    public WatchPreviewDto getWatchPreview(String userId) {
        // Watch 테이블에서 최근 10개 조회
        List<WatchVO> recentWatches = watchListMapper.findRecentWatches(userId, 10);

        List<WatchPreviewItemDto> watchItems = recentWatches.stream()
                .map(watch -> {
                    Object detail = null;
                    
                    // 상품 타입별로 상세 정보 조회
                    switch (watch.getProductCategory()) {
                        case "deposit":
                            DepositVO deposit = watchListMapper.findWatchDepositListByUserId(userId).stream()
                                    .filter(d -> d.getProductCode().equals(watch.getProductCode()))
                                    .findFirst()
                                    .orElse(null);
                            if (deposit != null) {
                                detail = getDepositObject(userId, deposit);
                            }
                            break;
                            
                        case "fund":
                            FundVO fund = watchListMapper.findWatchFundListByUserId(userId).stream()
                                    .filter(f -> f.getProductCode().equals(watch.getProductCode()))
                                    .findFirst()
                                    .orElse(null);
                            if (fund != null) {
                                detail = getFundObject(userId, fund);
                            }
                            break;
                            
                        case "etf":
                            EtfVO etf = watchListMapper.findWatchEtfListByUserId(userId).stream()
                                    .filter(e -> e.getProductCode().equals(watch.getProductCode()))
                                    .findFirst()
                                    .orElse(null);
                            if (etf != null) {
                                detail = getEtfObject(userId, etf);
                            }
                            break;
                    }

                    if (detail != null) {
                        return WatchPreviewItemDto.builder()
                                .watchListId(watch.getWatchlistId())
                                .productCategory(watch.getProductCategory())
                                .detail(detail)
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        
        return WatchPreviewDto.builder()
                .watchItems(watchItems)
                .totalCount(watchItems.size())
                .build();
    }

    private DepositByWatchDto getDepositObject(String userId, DepositVO deposit) {
        DOptionVO option = deposit.getDOption().stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));
        HoldingsVO holdingDeposit = holdingsMapper.findByUserAndProduct(userId, deposit.getProductCode());
        Boolean userOwn = holdingDeposit == null || holdingDeposit.getHoldingsStatus().equals("zero") ? Boolean.FALSE : Boolean.TRUE;

        return DepositByWatchDto.builder()
                .productCode(deposit.getProductCode())
                .productName(deposit.getProductName())
                .productCompanyName(deposit.getProductCompanyName())
                .userOwns(userOwn)
                .isPopularInUserGroup(Boolean.FALSE)
                .productRiskGrade(deposit.getProductRiskGrade())
                .depositIntrRate(option.getDOptionIntrRate())
                .depositIntrRate2(option.getDOptionIntrRate2())
                .build();
    }
    
    private EtfByWatchDto getEtfObject(String userId, EtfVO etf) {
        HoldingsVO holdingEtf = holdingsMapper.findByUserAndProduct(userId, etf.getProductCode());
        Boolean userOwn = holdingEtf == null || holdingEtf.getHoldingsStatus().equals("zero") ? Boolean.FALSE : Boolean.TRUE;
        NewsSentimentTotalDto newsSentiment = newsSentimentPer(newsMapper.findNewsSentimentByProductCode(etf.getProductCode()));
        Double etfNav = etfPriceService.getCurrent("etf_nav", etf.getProductCode());

        return EtfByWatchDto.builder()
                .productCode(etf.getProductCode())
                .productCountry(etf.getEtfCountry().getDbValue())
                .productCompanyName(etf.getProductCompanyName())
                .productType(etf.getEtfType().getDbValue())
                .productName(etf.getProductName())
                .userOwns(userOwn)
                .isPopularInUserGroup(Boolean.FALSE)
                .productRiskGrade(etf.getProductRiskGrade())
                .newsSentiment(newsSentiment)
                .etfNav(etfNav)
                .build();
    }

    private FundByWatchDto getFundObject(String userId, FundVO fund) {
        HoldingsVO holdingFund = holdingsMapper.findByUserAndProduct(userId, fund.getProductCode());
        Boolean userOwn = holdingFund == null || holdingFund.getHoldingsStatus().equals("zero") ? Boolean.FALSE : Boolean.TRUE;
        NewsSentimentTotalDto newsSentiment = newsSentimentPer(newsMapper.findNewsSentimentByProductCode(fund.getProductCode()));
        Double productRateOfReturn = etfPriceService.getPercentChangeFrom3MonthsAgo("fund_nav", fund.getProductCode());
        Double fundScale = etfPriceService.getCurrent("fund_aum", fund.getProductCode());
        Double currentNav = etfPriceService.getCurrent("fund_nav", fund.getProductCode());
        Double percentChangeFromYesterday = etfPriceService.getPercentChangeFromYesterday("fund_nav", fund.getProductCode());

        return FundByWatchDto.builder()
                .productCode(fund.getProductCode())
                .productCountry(fund.getFundCountry().getDbValue())
                .productCompanyName(fund.getProductCompanyName())
                .productType(fund.getFundType().getDbValue())
                .productName(fund.getProductName())
                .userOwns(userOwn)
                .isPopularInUserGroup(Boolean.FALSE)
                .productRiskGrade(fund.getProductRiskGrade())
                .newsSentiment(newsSentiment)
                .productRateOfReturn(productRateOfReturn)
                .fundScale(fundScale)
                .currentNav(currentNav)
                .percentChangeFromYesterday(percentChangeFromYesterday)
                .build();
    }
}
