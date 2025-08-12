package com.finsight.backend.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

/**
 * 사용자 뉴스 열람 로그를 저장하는 도큐먼트
 */

@Getter
@Setter
@Document(collection = "user_views")
public class UserView {

    @Id
    private String id;
    private String userId;      // 사용자 ID
    private String type;        // "news"
    private String targetId;    // 뉴스 ID
    private Date timestamp;     // 열람 시각
}
