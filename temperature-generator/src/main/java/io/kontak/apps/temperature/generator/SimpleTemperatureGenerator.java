package io.kontak.apps.temperature.generator;

import io.kontak.apps.event.TemperatureReading;
import io.kontak.apps.temperature.generator.model.Thermometer;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class SimpleTemperatureGenerator implements TemperatureGenerator {

    private static final Random random = new Random();

    private final double anomalyRate;
    private final List<Thermometer> thermometers;

    @Override
    public List<TemperatureReading> generate() {
        return thermometers.stream().map(this::generateSingleReading).toList();
    }

    private TemperatureReading generateSingleReading(Thermometer thermometer) {
        return new TemperatureReading(
                generateTemperature(),
                thermometer.getRoomId(),
                thermometer.getThermometerId(),
                Instant.now()
        );
    }

    private double generateTemperature() {

        var randomNumber = random.nextDouble();
        return (randomNumber < (1 - anomalyRate))
                ? getTemperature(false)
                : getTemperature(true);
        }

    private double getTemperature(boolean isAnomaly) {

        return isAnomaly
                ? random.nextInt(250, 360) / 10.0
                : random.nextInt(190, 200) / 10.0 ;
    }

}
