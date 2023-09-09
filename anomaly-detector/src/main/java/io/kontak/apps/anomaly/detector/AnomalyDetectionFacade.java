package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AnomalyDetectionFacade {

    @Autowired
    ObjectProvider<AnomalyDetector> anomalyDetectors;

    public Optional<Anomaly> detect(TemperatureReading temperatureReading) {

        return anomalyDetectors.stream()
                .map(anomalyDetector -> anomalyDetector.apply(temperatureReading))
                .findAny()
                .orElse(Optional.empty());
    }
}
