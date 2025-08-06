package com.finsight.backend.entity;

import com.finsight.backend.vo.Product;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "user_logs")
public class MonitoringLog {

    @Id
    private String id;

    private String userId;
    private String productCode;
    private Product product;
    private LocalDateTime accessTime = LocalDateTime.now();
}