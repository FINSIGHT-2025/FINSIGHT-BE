    package com.finsight.backend.vo.pricehistory;

    import lombok.AllArgsConstructor;
    import lombok.Getter;

    import java.time.Instant;

    @Getter
    @AllArgsConstructor
    public class PricePoint {
        private Instant time;
        private Double value;
    }
