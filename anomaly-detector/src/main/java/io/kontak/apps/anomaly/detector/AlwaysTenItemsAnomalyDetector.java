package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.OptionalDouble;

@Component("alwaysAnomalyAnomalyDetector")
public class AlwaysTenItemsAnomalyDetector implements AnomalyDetector {

    private final LinkedList<TemperatureReading> temperatureCache = new LinkedList<>();
    private TemperatureReading mostRecentAnomaly = createEmpty();

    @Override
    public Optional<Anomaly> apply(TemperatureReading temperatureReading) {

        temperatureCache.add(temperatureReading);

        if (temperatureCache.size() < 10) {
            return Optional.empty();
        }

        var max = getMaxTemperatureReading();
        var avg = getAverageTemperature();

        temperatureCache.poll();

        if (max == null || mostRecentAnomaly.equals(max) || avg.isEmpty()) {
            return Optional.empty();
        } else if (isAnomaly(max, avg)) {
            mostRecentAnomaly = max;
            return Optional.of(createAnomaly(max));
        } else {
            return Optional.empty();
        }
    }

    @NotNull
    private static Anomaly createAnomaly(TemperatureReading max) {
        return new Anomaly(max.temperature(), max.roomId() , max.thermometerId(), max.timestamp());
    }

    private boolean isAnomaly(TemperatureReading max, OptionalDouble avg) {
        return max.temperature() > avg.getAsDouble() + 5;
    }

    private OptionalDouble getAverageTemperature() {
        return temperatureCache.stream()
                .mapToDouble(TemperatureReading::temperature)
                .sorted()
                .skip(1)
                .average();
    }

    @Nullable
    private TemperatureReading getMaxTemperatureReading() {
        return temperatureCache.stream()
                .max(Comparator.comparingDouble(TemperatureReading::temperature))
                .orElse(null);
    }

    @NotNull
    private static TemperatureReading createEmpty() {
        return new TemperatureReading(Double.MIN_VALUE, null, null, null);
    }
}
