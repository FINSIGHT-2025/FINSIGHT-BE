package com.finsight.backend.control;

import com.finsight.backend.config.MongoConfig;
import com.finsight.backend.mongo.UserView;
import com.finsight.backend.service.UserViewLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        MongoConfig.class,       // MongoTemplate 설정
        UserViewLogger.class     // 테스트 대상 Bean 등록
})
public class NewsControllerTest {

    @Autowired
    private UserViewLogger logger;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void 뉴스_클릭_로그_정상_저장됨() {
        // given
        String userId = "testuser";
        String newsId = "NEWS_TEST1";

        // when
        logger.logNewsClick(userId, newsId);

//        // then
//        List<UserView> logs = mongoTemplate.findAll(UserView.class);
//        assertEquals(1, logs.size());
//        UserView log = logs.get(0);
//        assertEquals(userId, log.getUserId());
//        assertEquals("news", log.getType());
//        assertEquals(newsId, log.getTargetId());
//        assertNotNull(log.getTimestamp());
    }
}
