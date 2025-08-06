package com.finsight.backend.service;

import com.finsight.backend.dto.request.MonitoringRequest;
import com.finsight.backend.entity.MonitoringLog;
import com.finsight.backend.repository.MonitoringLogRepository;
import com.finsight.backend.vo.Deposit;
import com.finsight.backend.vo.Etf;
import com.finsight.backend.vo.Fund;
import com.finsight.backend.vo.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MonitoringLogServiceImpl implements MonitoringLogService{
    @Override
    public void saveProductUserLog(MonitoringRequest request) {

    }
}
