package io.kontak.apps.temperature.analytics;

import io.kontak.apps.event.Anomaly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AnomaliesFacade {

    @Value("${anomalies.threshold.default-value}")
    private Integer DEFAULT_THRESHOLD;
    @Autowired
    AnomaliesRepository repository;

    public Collection<Anomaly> getAnomaliesByRoomId(String roomId) {
        return repository.findAllByRoomId(roomId);
    }

    public Collection<Anomaly> getAnomaliesByThermometerId(String thermometerId) {
        return repository.findAllByThermometerId(thermometerId);
    }

    public Collection<String> getAnomaliesOverThreshold(Integer threshold) {
        return repository.findAllOverThreshold(getOrDefault(threshold));
    }

    private Integer getOrDefault(Integer threshold) {
        return threshold != null ? threshold : DEFAULT_THRESHOLD;
    }
}
