package com.finsight.backend.service.news;

import com.finsight.backend.domain.vo.product.EtfVO;
import com.finsight.backend.dto.external.NewsApiResponseDTO;
import com.finsight.backend.repository.mapper.EtfMapper;
import com.finsight.backend.repository.mapper.FundMapper;
import com.finsight.backend.service.fetcher.AssetType;
import com.finsight.backend.service.fetcher.NewsFetcher;
import com.finsight.backend.domain.vo.product.FStockHoldingsVO;
import com.finsight.backend.domain.vo.product.FundVO;
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
    private final FundMapper fundMapper;
    private final EtfMapper etfMapper;

    public NewsCoreService(List<NewsFetcher> fetchers, NewsSaveService newsSaveService, FundMapper fundMapper, EtfMapper etfMapper){
        this.newsSaveService = newsSaveService;
        this.fundMapper = fundMapper;

        Map<AssetType, NewsFetcher> map = new EnumMap<>(AssetType.class);
        for(NewsFetcher fetcher : fetchers){
            map.put(fetcher.supports(), fetcher);
        }
        this.fetcherMap = map;
        this.etfMapper = etfMapper;
    }

    /*
     *  스케줄러에 의해 호출되는 함수
     *  모든 자산(ETF, Fund)에 대한 뉴스 처리 작업
     */
    public void processAllAssets(){
        List<EtfVO> etfs = etfMapper.findEtfListByFilter(null, null, null, null, null);
        List<FundVO> funds = fundMapper.selectAllFundStock();

        processEtfsInternal(etfs);
        processFundsInternal(funds);
    }

    private void processEtfsInternal(List<EtfVO> etfs){
        NewsFetcher etfFetcher = findFetcher(AssetType.ETF);
        log.info("Starting ETF news processing for {} items.", etfs.size());

        for(EtfVO etf : etfs){
            try {
                etfFetcher.fetch(etf.getProductCode())
                        .doOnSuccess(response -> {
                            if (response != null && response.getData() != null) {
                                response.getData().forEach(newsData ->
                                        newsSaveService.saveNews(newsData, etf.getProductCode(), "etf")
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
    private void processFundsInternal(List<FundVO> funds) {
        NewsFetcher fundFetcher = findFetcher(AssetType.FUND);
        log.info("Starting Fund news processing for {} items.", funds.size());

        for (FundVO fund : funds) {
            try {
                // 보유 주식 정보가 없으면 다음 펀드로 넘어갑니다.
                if (fund.getFStockHoldings() == null || fund.getFStockHoldings().isEmpty()) {
                    continue;
                }

                // 키워드가 될 주식 종목명 리스트를 추출합니다.
                List<String> keywords = fund.getFStockHoldings().stream()
                        .map(FStockHoldingsVO::getFStockHoldingsName)
                        .limit(2)
                        .toList();

                if (keywords.isEmpty()) {
                    continue;
                }

                // 2. fetcher를 호출하고 .block()을 사용해 응답이 올 때까지 기다립니다.
                //    이 부분이 동기 블로킹으로 동작하는 핵심입니다.
                NewsApiResponseDTO response = fundFetcher.fetch(keywords).block();

                // 3. 응답을 받은 후, 데이터를 저장합니다.
                if (response != null && response.getData() != null) {
                    log.info("Saving {} news items for fund: {}", response.getData().size(), fund.getProductCode());
                    response.getData().forEach(newsData ->
                            newsSaveService.saveNews(newsData, fund.getProductCode(), "fund")
                    );
                }

            } catch (Exception e) {
                // 4. 특정 펀드 처리 중 에러가 발생해도 전체 작업이 멈추지 않도록 try-catch로 감쌉니다.
                log.error("Failed to process Fund {} due to an error.", fund.getProductCode(), e);
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
