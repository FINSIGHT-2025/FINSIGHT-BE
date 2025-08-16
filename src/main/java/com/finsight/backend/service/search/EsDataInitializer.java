package com.finsight.backend.service.search;

import com.finsight.backend.client.EsProductIndexer;
import com.finsight.backend.repository.mapper.SearchEsProductExportMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class EsDataInitializer {

    private final SearchEsProductExportMapper mapper;
    private final EsProductIndexer indexer;

    @PostConstruct
    public void init() {
        log.info("[ES] Indexing on startup - begin");

        var deposits = mapper.selectDepositsForEs();
        log.info("[ES] Deposit rows: {}", deposits.size());
        indexer.indexDeposits(deposits);

        var funds = mapper.selectFundsForEs();
        log.info("[ES] Fund rows: {}", funds.size());
        indexer.indexFunds(funds);

        var etfs = mapper.selectEtfsForEs();
        log.info("[ES] ETF rows: {}", etfs.size());
        indexer.indexEtfs(etfs);

        log.info("[ES] Indexing on startup - done");
    }
}
