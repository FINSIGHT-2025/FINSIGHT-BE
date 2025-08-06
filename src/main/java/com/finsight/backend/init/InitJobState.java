package com.finsight.backend.init;

import org.springframework.stereotype.Component;

@Component
public class InitJobState {
    private boolean fundInitCompleted;
    private boolean etfInitCompleted;

    public boolean isFundInitCompleted() {
        return fundInitCompleted;
    }

    public void markFundInitCompleted() {
        this.fundInitCompleted = true;
    }

    public boolean isEtfInitCompleted() {
        return etfInitCompleted;
    }

    public void markEtfInitCompleted() {
        this.etfInitCompleted = true;
    }

}
