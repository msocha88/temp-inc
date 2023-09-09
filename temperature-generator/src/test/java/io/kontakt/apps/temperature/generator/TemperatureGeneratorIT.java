package io.kontakt.apps.temperature.generator;

import io.kontak.apps.event.TemperatureReading;
import io.kontak.apps.temperature.generator.TemperatureGeneratorProperties;
import io.kontak.apps.temperature.generator.TemperatureStreamPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.Instant;

public class TemperatureGeneratorIT extends AbstractIntegrationTest {

    @Autowired
    private TemperatureStreamPublisher publisher;

    @Value("${spring.cloud.stream.bindings.messageProducer-out-0.destination}")
    private String topic;

    @Autowired
    TemperatureGeneratorProperties properties;

    @Test
    void testRecordPublishing() {

        try (TestKafkaConsumer<TemperatureReading> consumer = new TestKafkaConsumer<>(
                kafkaContainer.getBootstrapServers(),
                topic,
                TemperatureReading.class
        )) {
            consumer.drain(
                    consumerRecords -> consumerRecords.size() >= properties.getThermometers().size(),
                    Duration.ofSeconds(5)
            );
        }
    }
}
