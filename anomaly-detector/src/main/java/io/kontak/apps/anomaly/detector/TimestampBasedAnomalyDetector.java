package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Component("timestampBasedAnomalyDetector")
public class TimestampBasedAnomalyDetector implements AnomalyDetector {

    private static final long WINDOW_SIZE_IN_SECONDS = 10;
    private final List<TemperatureReading> temperatureCache = new ArrayList<>();
    private final List<TemperatureReading> anomaliesDetectedBefore = new ArrayList<>();

    @Override
    public Optional<Anomaly> apply(TemperatureReading temperatureReading) {

        temperatureCache.add(temperatureReading);
        var windowBegin = temperatureReading.timestamp().minusSeconds(WINDOW_SIZE_IN_SECONDS);

        anomaliesDetectedBefore.removeIf(reading -> reading.timestamp().isBefore(windowBegin));
        var avg = getAverageTemperature();

        if (avg.isEmpty()) {
            return Optional.empty();
        }

        var detectedAnomaly = temperatureCache.stream()
                .filter(reading -> !reading.timestamp().isBefore(windowBegin))
                .filter(reading -> reading.temperature() > avg.getAsDouble() + 5)
                .filter(anomaly -> !anomaliesDetectedBefore.contains(anomaly))
                .map(this::createAnomaly)
                .findFirst();

        if (detectedAnomaly.isPresent()) {
            anomaliesDetectedBefore.add(temperatureReading);
        }

        return detectedAnomaly;
    }

    private Anomaly createAnomaly(TemperatureReading max) {
        return new Anomaly(max.temperature(), max.roomId(), max.thermometerId(), max.timestamp());
    }

    private OptionalDouble getAverageTemperature() {
        return temperatureCache.stream()
                .mapToDouble(TemperatureReading::temperature)
                .average();
    }
}
