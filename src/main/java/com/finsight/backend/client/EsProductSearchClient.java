package com.finsight.backend.client;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EsProductSearchClient {

    private final ElasticsearchClient es;

    @Value("${search.minScore:0.2}") private double minScore;
    @Value("${search.fuzzinessMinLen:2}") private int fuzzinessMinLen;

    private String indexFor(String category) {
        return switch (category) {
            case "deposit" -> "deposit-products";
            case "fund"    -> "fund-products";
            case "etf"     -> "etf-products";
            default        -> "finsight-products";
        };
    }

    private boolean useFuzzy(String q) {
        return q != null && q.trim().length() >= fuzzinessMinLen;
    }

    public EsPage search(String category, String q, int page, int size) {
        int from = page * size;
        boolean fuzz = useFuzzy(q);

        try {
            SearchResponse<Map> res = es.search(s -> s
                            .index(indexFor(category))
                            .from(from).size(size)
                            .minScore(minScore)
                            .source(src -> src.filter(f -> f.includes(
                                    "productCode","productName","productRiskGrade","domain",
                                    "productCompanyName",
                                    "productCountry","productType"
                            )))
                            .query(qb -> qb.bool(b -> {
                                b.minimumShouldMatch("1");

                                // 코드: 정확/접두
                                b.should(sh -> sh.term(t -> t.field("productCode")
                                        .value(v -> v.stringValue(q)).boost(50f)));
                                b.should(sh -> sh.prefix(p -> p.field("productCode")
                                        .value(q).boost(20f)));

                                // 이름: 정확/구/접두/ngram/오타
                                b.should(sh -> sh.term(t -> t.field("productName.keyword")
                                        .value(v -> v.stringValue(q)).boost(40f)));
                                b.should(sh -> sh.matchPhrase(mp -> mp.field("productName")
                                        .query(q).boost(12f)));
                                b.should(sh -> sh.matchPhrasePrefix(mpp -> mpp.field("productName.ngram")
                                        .query(q).boost(8f)));

                                if (fuzz) {
                                    b.should(sh -> sh.match(m -> m.field("productName")
                                            .query(q).fuzziness("AUTO").prefixLength(1).boost(3f)));
                                }
                                return b;
                            }))
                            .sort(srt -> srt.score(sc -> sc.order(SortOrder.Desc)))
                            .sort(srt -> srt.field(f -> f.field("productName.keyword").order(SortOrder.Asc))),
                    Map.class
            );

            long total = Optional.ofNullable(res.hits().total()).map(t -> t.value()).orElse(0L);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> docs = res.hits().hits().stream()
                    .map(h -> (Map<String, Object>) h.source())
                    .toList();

            return new EsPage(total, docs);
        } catch (Exception e) {
            throw new RuntimeException("Elasticsearch search failed", e);
        }
    }

    public record EsPage(long total, List<Map<String, Object>> docs) {}
}
