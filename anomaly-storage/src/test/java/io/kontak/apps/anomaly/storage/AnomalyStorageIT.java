package io.kontak.apps.anomaly.storage;

import io.kontak.apps.event.Anomaly;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class AnomalyStorageIT extends AbstractIntegrationTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Value("${spring.cloud.stream.bindings.anomalyConsumer-in-0.destination}")
    private String inputTopic;

    @Test
    @SneakyThrows
    void testInOutFlow() {
        try (TestKafkaProducer<Anomaly> producer = new TestKafkaProducer<>(
                     kafkaContainer.getBootstrapServers(),
                     inputTopic
             )) {
            var roomId = UUID.randomUUID().toString();
            var thermometerId = UUID.randomUUID().toString();
            Anomaly anomaly = new Anomaly( 36, roomId, thermometerId, Instant.now());
            producer.produce(anomaly.thermometerId(), anomaly);
            Thread.sleep(2000);

            var storedAnomalies = mongoTemplate.find(
                    query(where("roomId").is(roomId).and("thermometerId").is(thermometerId)),
                    Anomaly.class);

            assertThat(storedAnomalies.stream().anyMatch(a -> a.thermometerId().equals(thermometerId) && a.roomId().equals(roomId)))
                    .isTrue();
        }
    }
}
