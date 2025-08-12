    package com.finsight.backend.domain.vo.product;

    import lombok.AllArgsConstructor;
    import lombok.Getter;

    import java.time.Instant;

    @Getter
    @AllArgsConstructor
    public class PricePointVO {
        private Instant time;
        private double value;
    }
