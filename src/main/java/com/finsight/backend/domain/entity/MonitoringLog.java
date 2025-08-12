package com.finsight.backend.domain.entity;

import com.finsight.backend.domain.vo.product.ProductVO;
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
    private ProductVO product;
    private LocalDateTime accessTime = LocalDateTime.now();
}