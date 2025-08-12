package com.finsight.backend.repository;

import com.finsight.backend.domain.entity.MonitoringLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MonitoringLogRepository extends MongoRepository<MonitoringLog, String> {
}
