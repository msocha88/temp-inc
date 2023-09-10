package io.kontak.apps.anomaly.storage;

import io.kontak.apps.event.Anomaly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageFacade {

    @Autowired
    private AnomalyRepository anomalyRepository;

    public void store(Anomaly anomaly) {
        anomalyRepository.store(anomaly);
    }

}
