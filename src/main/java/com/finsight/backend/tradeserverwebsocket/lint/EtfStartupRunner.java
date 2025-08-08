package com.finsight.backend.tradeserverwebsocket.lint;

import com.finsight.backend.tradeserverwebsocket.service.scheduler.EtfCacheScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EtfStartupRunner implements SmartLifecycle {

    private final EtfCacheScheduler etfCacheScheduler;
    private boolean isRunning = false;

    @Override
    public void start() {
        // 서버 완전 기동 후 실행
        etfCacheScheduler.updateAllEtfHistoricalPrices();
        isRunning = true;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE; // 최대 값으로 설정하면 가장 마지막에 실행됨
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        isRunning = false;
        callback.run();
    }

    @Override
    public void stop() {
        isRunning = false;
    }
}
