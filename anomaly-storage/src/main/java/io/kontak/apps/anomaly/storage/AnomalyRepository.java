package io.kontak.apps.anomaly.storage;

import io.kontak.apps.event.Anomaly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AnomalyRepository{

    @Autowired
    MongoTemplate mongoTemplate;

    public void store(Anomaly anomaly) {
        mongoTemplate.save(anomaly);
    }

}
