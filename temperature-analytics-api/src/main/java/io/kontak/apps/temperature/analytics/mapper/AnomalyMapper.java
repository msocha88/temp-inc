package io.kontak.apps.temperature.analytics.mapper;

import io.kontak.apps.db.AnomalyDao;
import io.kontak.apps.event.Anomaly;
import org.springframework.stereotype.Component;

@Component
public class AnomalyMapper {

    public Anomaly map(AnomalyDao dao) {
        return new Anomaly(
                dao.temperature(),
                dao.roomId(),
                dao.thermometerId(),
                dao.timestamp()
        );
    }

}
