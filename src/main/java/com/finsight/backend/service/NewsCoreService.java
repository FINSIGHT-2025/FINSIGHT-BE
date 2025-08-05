package com.finsight.backend.service;

import com.finsight.backend.mapper.NewsMapper;
import com.finsight.backend.service.fetcher.AssetType;
import com.finsight.backend.service.fetcher.NewsFetcher;
import com.finsight.backend.vo.TempEtfVO;
import com.finsight.backend.vo.TempFundVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * DB에서 상품 목록 조회 & 각 상품에 맞는 Fetcher를 찾아 뉴스 수집 및 저장 지시
 */
@Slf4j
@Service
public class NewsCoreService {
    private final NewsSaveService newsSaveService;
    private final Map<AssetType, NewsFetcher> fetcherMap;
    private final NewsMapper newsMapper;

    public NewsCoreService(List<NewsFetcher> fetchers, NewsSaveService newsSaveService, NewsMapper newsMapper){
        this.newsSaveService = newsSaveService;
        this.newsMapper = newsMapper;

        Map<AssetType, NewsFetcher> map = new EnumMap<>(AssetType.class);
        for(NewsFetcher fetcher : fetchers){
            map.put(fetcher.supports(), fetcher);
        }
        this.fetcherMap = map;
    }

    /*
     *  스케줄러에 의해 호출되는 함수
     *  모든 자산(ETF, Fund)에 대한 뉴스 처리 작업
     */
    public void processAllAssets(){
        List<TempEtfVO> etfs = newsMapper.selectAllEtfs();
        List<TempFundVO> funds = newsMapper.selectAllFunds();

        processEtfsInternal(etfs);
        processFundsInternal(funds);
    }

    private void processEtfsInternal(List<TempEtfVO> etfs){
        NewsFetcher etfFetcher = findFetcher(AssetType.ETF);
        log.info("Starting ETF news processing for {} items.", etfs.size());

        for(TempEtfVO etf : etfs){
            try {
                // .subscribe() 대신 .block()을 사용하여 동기식으로 처리
                etfFetcher.fetch(etf.getProductCode())
                        .doOnSuccess(response -> {
                            if (response != null && response.getData() != null) {
                                response.getData().forEach(newsData ->
                                        newsSaveService.saveNews(newsData, etf.getProductCode())
                                );
                            }
                        })
                        .doOnError(error -> log.error("Error fetching news for ETF: {}", etf.getProductCode(), error))
                        .block(); // 작업이 끝날 때까지 여기서 대기
            } catch (Exception e) {
                log.error("Failed to process ETF {} due to an error in the reactive stream.", etf.getProductCode(), e);
            }
        }
    }
    private void processFundsInternal(List<TempFundVO> funds) {
        NewsFetcher fundFetcher = findFetcher(AssetType.FUND);
        log.info("Starting Fund news processing for {} items.", funds.size());

        for (TempFundVO fund : funds) {
            if (fund.getFundStockHoldings() == null || fund.getFundStockHoldings().isBlank()) {
                continue;
            }
            try {
                // ❗️ .subscribe() 대신 .block()을 사용하여 동기식으로 처리
                fundFetcher.fetch(fund.getFundStockHoldings())
                        .doOnSuccess(response -> {

                            // 임시 로그 코드
                            if (response == null) {
                                log.warn("Response is null for fund: {}", fund.getProductCode());
                                return;
                            }

                            if (response.getData() == null || response.getData().isEmpty()) {
                                log.warn("No news data returned for fund: {}", fund.getProductCode());
                                return;
                            }

                            log.info("Fetched {} news items for fund: {}", response.getData().size(), fund.getProductCode());


                            // ----

                            if (response != null && response.getData() != null) {
                                response.getData().forEach(newsData ->
                                        newsSaveService.saveNews(newsData, fund.getProductCode())
                                );
                            }
                        })
                        .doOnError(error -> log.error("Error fetching news for Fund: {}", fund.getProductCode(), error))
                        .block(); // 작업이 끝날 때까지 여기서 대기
            } catch (Exception e) {
                log.error("Failed to process Fund {} due to an error in the reactive stream.", fund.getProductCode(), e);
            }
        }
    }

    private NewsFetcher findFetcher(AssetType assetType) {
        NewsFetcher fetcher = fetcherMap.get(assetType);
        if (fetcher == null) {
            throw new IllegalStateException("Unsupported asset type: " + assetType);
        }
        return fetcher;
    }
}
