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

public class TimeBasedAnomalyDetectorTest {

    private final AnomalyDetector tested = new TimestampBasedAnomalyDetector();
    private static final String roomId = UUID.randomUUID().toString();
    private static final String thermometerId = UUID.randomUUID().toString();

    @Test
    public void shouldReturnNotEmptyAnomaly() {

        //given
        var anomalousTemperature = 27.1;

        var temperatures = List.of(
                createReading(20.1, 0),
                createReading(anomalousTemperature, 2),
                createReading(20.1,3),
                createReading(20.1,6)
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
                createReading(20.1,0),
                createReading(27.1,1),
                createReading(20.1,2),
                createReading(20.1,3),
                createReading(31.1,5)
        );

        //when
        var result = temperatures.stream()
                .map(tested::apply)
                .filter(Optional::isPresent)
                .toList();

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void shouldNotReturnAnomaly() {

        //given
        var temperatures = List.of(
                createReading(20.1,0),
                createReading(21.2,1),
                createReading(20.3,2),
                createReading(19.1,3),
                createReading(20.1,4),
                createReading(19.2,5),
                createReading(18.1,6),
                createReading(19.1,7),
                createReading(20.1,8),
                createReading(21.1,9),
                createReading(23.1,10)
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
    public void shouldNotReturnAnomalyOlderThan10Seconds() {

        //given

        var temperatures = List.of(
                createReading(42.1,15),
                createReading(20.1,0),
                createReading(21.2,1),
                createReading(20.3,2)
        );

        //when
        var result = temperatures.stream()
                .map(temp -> tested.apply(temp))
                .filter(Optional::isPresent)
                .toList();

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @NotNull
    private static TemperatureReading createReading(double temperature, int minusSeconds) {
        return new TemperatureReading(temperature, roomId, thermometerId, Instant.now().minusSeconds(minusSeconds));
    }


}
