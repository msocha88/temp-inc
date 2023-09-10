package io.kontak.apps.anomaly.storage;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = AnomalyStorageApplication.class)
@Testcontainers
public class AbstractIntegrationTest {

    public final static KafkaContainer kafkaContainer;
    public final static MongoDBContainer mongoContainer;

    static {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));
        kafkaContainer.start();
        mongoContainer = new MongoDBContainer("mongo:6-jammy").withExposedPorts(27017);
        mongoContainer.start();
    }

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.stream.binders.kafka.environment.spring.cloud.stream.kafka.streams.binder.brokers", kafkaContainer::getBootstrapServers);
        registry.add("spring.data.mongodb.uri", mongoContainer::getReplicaSetUrl);
    }

}
