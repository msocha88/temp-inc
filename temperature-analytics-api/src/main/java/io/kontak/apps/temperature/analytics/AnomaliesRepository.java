package io.kontak.apps.temperature.analytics;

import io.kontak.apps.db.AnomalyDao;
import io.kontak.apps.event.Anomaly;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public interface AnomaliesRepository extends MongoRepository<AnomalyDao, String> {

    Collection<AnomalyDao> findAllByRoomId(String thermometerId);
    Collection<AnomalyDao> findAllByThermometerId(String thermometerId);

    @Aggregation(pipeline = {
            "{$group: {'_id' : '$thermometerId', count:{$sum:1}}}",
            "{$match: {'count': {$gt: ?0}}}",
            "{$project:{'_id' : 1}}"
    })
    Collection<String> findAllOverThreshold(Integer threshold);
}
