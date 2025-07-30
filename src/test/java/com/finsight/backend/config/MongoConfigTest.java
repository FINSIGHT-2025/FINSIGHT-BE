package com.finsight.backend.config;

import com.mongodb.client.MongoClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = MongoConfig.class)
class MongoConfigTest {
    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void mongoClientShouldBeNotNull(){
        assertNotNull(mongoClient);
    }
    @Test
    void mongoTemplateShouldBeNotNull() {
        assertNotNull(mongoTemplate);
    }

    @Test
    void mongoClientShouldConnectToDatabase(){
        ArrayList<String> dbs = mongoClient.listDatabaseNames().into(new ArrayList<>());
        assertFalse(dbs.isEmpty());
        for (String db : dbs) {
            System.out.println(db);
        }
    }

}