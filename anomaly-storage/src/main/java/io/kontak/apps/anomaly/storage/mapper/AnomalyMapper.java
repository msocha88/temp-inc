package io.kontak.apps.anomaly.storage.mapper;

import io.kontak.apps.db.AnomalyDao;
import io.kontak.apps.event.Anomaly;
import org.springframework.stereotype.Component;

@Component
public class AnomalyMapper {

    public AnomalyDao map(Anomaly anomaly) {
        return new AnomalyDao(anomaly.temperature(), anomaly.roomId(), anomaly.thermometerId(), anomaly.timestamp());
    }

}
