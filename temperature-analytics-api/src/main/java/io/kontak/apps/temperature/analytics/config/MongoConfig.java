package io.kontak.apps.temperature.analytics.config;

import io.kontak.apps.event.Anomaly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import javax.annotation.PostConstruct;

@Configuration
public class MongoConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void createMongoIndexes() {
        mongoTemplate.indexOps(Anomaly.class)
                .ensureIndex(new Index().on("roomId", Sort.Direction.ASC));
        mongoTemplate.indexOps(Anomaly.class)
                .ensureIndex(new Index().on("thermometerId", Sort.Direction.ASC));
    }

}
