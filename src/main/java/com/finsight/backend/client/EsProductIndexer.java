package com.finsight.backend.client;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EsProductIndexer {
    private final ElasticsearchClient es;

    public void indexDeposits(List<Map<String, Object>> rows) {
        bulk("deposit-products", rows, "productCode");
    }
    public void indexFunds(List<Map<String, Object>> rows) {
        bulk("fund-products", rows, "productCode");
    }
    public void indexEtfs(List<Map<String, Object>> rows) {
        bulk("etf-products", rows, "productCode");
    }

    private void bulk(String index, List<Map<String, Object>> rows, String idField) {
        if (rows == null || rows.isEmpty()) return;

        try {
            // 청크로 끊어서 전송 (대용량 안전)
            int chunkSize = 1000;
            for (int i = 0; i < rows.size(); i += chunkSize) {
                var slice = rows.subList(i, Math.min(i + chunkSize, rows.size()));
                BulkRequest.Builder br = new BulkRequest.Builder();
                for (Map<String, Object> src : slice) {
                    Object risk = src.get("productRiskGrade");
                    if (risk instanceof String s) {
                        try { src.put("productRiskGrade", Integer.parseInt(s)); } catch (Exception ignore) {}
                    }
                    String id = String.valueOf(src.get(idField));
                    br.operations(op -> op.index(idx -> idx.index(index).id(id).document(src)));
                }
                var resp = es.bulk(br.build());
                if (resp.errors()) {
                    var firstErr = resp.items().stream().filter(j -> j.error() != null).findFirst().orElse(null);
                    throw new RuntimeException("Bulk has errors: " + (firstErr != null ? firstErr.error().reason() : ""));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Bulk indexing failed for index=" + index, e);
        }
    }
}
