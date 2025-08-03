package com.finsight.backend.service;

import com.finsight.backend.dto.request.MonitoringRequest;

public interface MonitoringLogService {
    void saveProductUserLog(MonitoringRequest request);
}
