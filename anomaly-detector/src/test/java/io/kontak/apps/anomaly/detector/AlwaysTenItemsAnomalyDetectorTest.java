package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.TemperatureReading;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AlwaysTenItemsAnomalyDetectorTest {

    private final AnomalyDetector tested = new AlwaysTenItemsAnomalyDetector();
    private static final String roomId = UUID.randomUUID().toString();
    private static final String thermometerId = UUID.randomUUID().toString();

    @Test
    public void shouldReturnNotEmptyAnomaly() {

        //given
        double anomalousTemperature = 27.1;
        var temperatures = List.of(
                createReading(20.1),
                createReading(21.2),
                createReading(20.3),
                createReading(19.1),
                createReading(20.1),
                createReading(19.2),
                createReading(18.1),
                createReading(19.1),
                createReading(20.1),
                createReading(anomalousTemperature),
                createReading(23.1)
        );

        //when
        var result = temperatures.stream()
                .map(tested::apply)
                .filter(Optional::isPresent)
                .toList();

        //then
        assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0)).isPresent();
        assertThat(result.get(0).get().temperature()).isEqualTo(anomalousTemperature);

    }

    @Test
    public void shouldReturnMoreThanOneDistinctAnomaly() {

        //given
        var temperatures = List.of(
                createReading(20.1),
                createReading(32.1),
                createReading(20.1),
                createReading(20.1),
                createReading(20.1),
                createReading(20.1),
                createReading(20.1),
                createReading(20.2),
                createReading(20.3),
                createReading(21.1),
                createReading(26.1),
                createReading(19.2),
                createReading(18.1),
                createReading(19.1),
                createReading(20.1),
                createReading(29.1),
                createReading(23.1)
        );

        //when
        var result = temperatures.stream()
                .map(tested::apply)
                .filter(Optional::isPresent)
                .toList();

        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    public void shouldNotReturnAnomaly() {

        //given
        var roomId = UUID.randomUUID().toString();
        var thermometerId = UUID.randomUUID().toString();

        var temperatures = List.of(
                createReading(20.1),
                createReading(21.2),
                createReading(20.3),
                createReading(19.1),
                createReading(20.1),
                createReading(19.2),
                createReading(18.1),
                createReading(19.1),
                createReading(20.1),
                createReading(21.1),
                createReading(23.1)
        );

        //when
        var result = temperatures.stream()
                .map(tested::apply)
                .filter(Optional::isPresent)
                .toList();

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    public void shouldNotReturnAnomalyAsThereIsLessThan10Inputs() {

        //given
        var roomId = UUID.randomUUID().toString();
        var thermometerId = UUID.randomUUID().toString();

        var temperatures = List.of(
                createReading(20.1),
                createReading(21.2),
                createReading(20.3),
                createReading(42.1));

        //when
        var result = temperatures.stream()
                .map(tested::apply)
                .filter(Optional::isPresent)
                .toList();

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @NotNull
    private static TemperatureReading createReading(double temperature) {
        return new TemperatureReading(temperature, roomId, thermometerId, Instant.now());
    }
}
