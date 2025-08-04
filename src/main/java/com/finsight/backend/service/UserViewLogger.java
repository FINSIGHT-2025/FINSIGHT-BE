package com.finsight.backend.service;

import com.finsight.backend.mongo.UserView;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.Date;

public class UserViewLogger {

    private final MongoTemplate mongoTemplate;

    public UserViewLogger(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 뉴스 클릭 로그 저장
     */
    public void logNewsClick(String userId, String newsId) {
        UserView view = new UserView();
        view.setUserId(userId);
        view.setType("news");
        view.setTargetId(newsId);
        view.setTimestamp(new Date());

        mongoTemplate.insert(view);
    }
}
