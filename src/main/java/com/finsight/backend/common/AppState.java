package com.finsight.backend.common;

import org.springframework.stereotype.Component;

@Component
public class AppState {
    private boolean fundDailyInitCompleted;
    private boolean etfDailyInitCompleted;
    private boolean etfRealtimeInitCompleted;

    public boolean isFundDailyInitCompleted() {
        return fundDailyInitCompleted;
    }

    public void markFundDailyInitCompleted() {
        this.fundDailyInitCompleted = true;
    }

    public boolean isEtfDailyInitCompleted() {
        return etfDailyInitCompleted;
    }

    public void markEtfDailyInitCompleted() {
        this.etfDailyInitCompleted = true;
    }

    public boolean isEtfRealtimeInitCompleted() {
        return etfRealtimeInitCompleted;
    }

    public void markEtfRealtimeInitCompleted() {
        this.etfRealtimeInitCompleted = true;
    }
}
