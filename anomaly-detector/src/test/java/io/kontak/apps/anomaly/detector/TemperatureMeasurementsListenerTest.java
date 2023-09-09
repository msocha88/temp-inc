package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TemperatureMeasurementsListenerTest extends AbstractIntegrationTest {

    @Value("${spring.cloud.stream.bindings.anomalyDetectorProcessor-in-0.destination}")
    private String inputTopic;

    @Value("${spring.cloud.stream.bindings.anomalyDetectorProcessor-out-0.destination}")
    private String outputTopic;

    @Test
    void testInOutFlow() {
        try (TestKafkaConsumer<Anomaly> consumer = new TestKafkaConsumer<>(
                kafkaContainer.getBootstrapServers(),
                outputTopic,
                Anomaly.class
        );
             TestKafkaProducer<TemperatureReading> producer = new TestKafkaProducer<>(
                     kafkaContainer.getBootstrapServers(),
                     inputTopic
             )) {

            var constantTemp = 20d;

            var temperatureReadingList = provideConstantTempReadings(constantTemp,10);
            TemperatureReading anomaly = new TemperatureReading(constantTemp + 6, "room", "thermometer", Instant.now());
            temperatureReadingList.add(anomaly);

            temperatureReadingList.forEach( tr ->producer.produce(tr.thermometerId(), tr));
            consumer.drain(
                    consumerRecords -> consumerRecords.stream().anyMatch(r -> r.value().thermometerId().equals(anomaly.thermometerId())),
                    Duration.ofSeconds(5)
            );
        }
    }

    @NotNull
    private static List<TemperatureReading> provideConstantTempReadings(double temp, int objectsToProvide) {
        var result = new ArrayList<TemperatureReading>();
        var now = Instant.now();
        while ( objectsToProvide-- > 0 ) {
            result.add(new TemperatureReading(temp, "room", "thermometer", now.minusSeconds(objectsToProvide)));
        }
        return result;
    }
}
